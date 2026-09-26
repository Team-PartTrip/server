package com.example.PartTrip.planner.service;

import com.example.PartTrip.global.exception.AiUnavailableException;
import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.main.service.TourPlaceImportService;
import com.example.PartTrip.planner.dto.request.GeneratePlannerRequestDto;
import com.example.PartTrip.planner.entity.PlannerScheduleSlotEntity;
import com.example.PartTrip.planner.enums.PlannerBlockType;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.PlannerScheduleSlotRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.profile.dto.TravelPreferenceResponseDto;
import com.example.PartTrip.profile.enums.PreferredTransport;
import com.example.PartTrip.profile.service.TravelPreferenceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlannerDraftServiceTest {

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final LocalDate D1 = LocalDate.of(2026, 10, 10);
    private static final LocalDate D2 = LocalDate.of(2026, 10, 11);

    @Mock private TourPlaceImportService tourPlaceImportService;
    @Mock private TourPlaceRepository tourPlaceRepository;
    @Mock private TravelPreferenceService travelPreferenceService;
    @Mock private OpenAiClient openAiClient;
    @Mock private PlannerService plannerService;
    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private PlannerScheduleSlotRepository slotRepository;
    @Mock private TransactionTemplate transactionTemplate;
    @Spy private ObjectMapper objectMapper = new ObjectMapper();
    @InjectMocks private PlannerDraftService service;

    private static JsonNode json(String s) throws Exception {
        return JSON.readTree(s);
    }

    // ── AI 답 검사 ──────────────────────────────────────────

    @Test
    void 후보에_없는_장소와_중복은_버리고_빈_칸으로_둔다() throws Exception {
        JsonNode answer = json("""
                {"days":[{"date":"2026-10-10","placeIds":[1, 999, 1, 2]},
                         {"date":"2026-10-11","placeIds":[2, 3]}]}""");

        List<List<Long>> days = PlannerDraftService.toSlots(
                answer, List.of(D1, D2), 3, Set.of(1L, 2L, 3L), List.of());

        // 999 는 AI 가 지어낸 id, 두 번째 1 과 둘째 날 2 는 중복이다
        assertThat(days.get(0)).containsExactly(1L, 2L, null);
        assertThat(days.get(1)).containsExactly(3L, null, null);
    }

    @Test
    void 하루_칸_수를_넘으면_자른다() throws Exception {
        JsonNode answer = json("""
                {"days":[{"date":"2026-10-10","placeIds":[1, 2, 3]}]}""");

        assertThat(PlannerDraftService.toSlots(answer, List.of(D1), 2, Set.of(1L, 2L, 3L), List.of()))
                .containsExactly(List.of(1L, 2L));
    }

    @Test
    void 날짜가_틀리게_오면_순서대로_맞춘다() throws Exception {
        JsonNode answer = json("""
                {"days":[{"date":"10월 10일","placeIds":[1]},{"date":"10월 11일","placeIds":[2]}]}""");

        List<List<Long>> days = PlannerDraftService.toSlots(
                answer, List.of(D1, D2), 1, Set.of(1L, 2L), List.of());

        assertThat(days).containsExactly(List.of(1L), List.of(2L));
    }

    @Test
    void 답이_엉망이어도_빈_카드로_채운다() throws Exception {
        List<List<Long>> days = PlannerDraftService.toSlots(
                json("{\"oops\":true}"), List.of(D1, D2), 2, Set.of(1L), List.of());

        assertThat(days).containsExactly(
                Arrays.asList(null, null), Arrays.asList(null, null));
    }

    @Test
    void 반드시_포함할_곳이_빠졌으면_빈_칸에_넣는다() throws Exception {
        JsonNode answer = json("""
                {"days":[{"date":"2026-10-10","placeIds":[1]}]}""");

        assertThat(PlannerDraftService.toSlots(answer, List.of(D1), 2, Set.of(1L, 7L), List.of(7L)))
                .containsExactly(List.of(1L, 7L));
    }

    @Test
    void 빈_칸이_없어도_반드시_포함할_곳은_넣는다() throws Exception {
        JsonNode answer = json("""
                {"days":[{"date":"2026-10-10","placeIds":[1, 2]}]}""");

        assertThat(PlannerDraftService.toSlots(answer, List.of(D1), 2, Set.of(1L, 2L, 7L), List.of(7L)))
                .containsExactly(List.of(1L, 7L));
    }

    @Test
    void 날짜로_이미_맞춘_답을_다른_날에_또_쓰지_않는다() throws Exception {
        // 첫 답은 날짜가 틀렸고 둘째 답이 첫날이다. 둘째 답을 둘째 날에 또 쓰면 2 가 엉뚱한 날로 간다
        JsonNode answer = json("""
                {"days":[{"date":"invalid","placeIds":[99]},{"date":"2026-10-10","placeIds":[1, 2]}]}""");

        List<List<Long>> days = PlannerDraftService.toSlots(
                answer, List.of(D1, D2), 1, Set.of(1L, 2L, 99L), List.of());

        assertThat(days).containsExactly(List.of(1L), List.of(99L));
    }

    @Test
    void 빈_칸보다_필수_장소가_많아도_앞의_필수_장소를_덮지_않는다() throws Exception {
        JsonNode answer = json("""
                {"days":[{"date":"2026-10-10","placeIds":[1]}]}""");

        List<List<Long>> days = PlannerDraftService.toSlots(
                answer, List.of(D1), 2, Set.of(1L, 7L, 8L), List.of(7L, 8L));

        assertThat(days.get(0)).containsExactlyInAnyOrder(7L, 8L);
    }

    @Test
    void 숙소는_후보_안에서만_받는다() throws Exception {
        assertThat(PlannerDraftService.pickLodging(json("{\"lodgingId\":5}"), Set.of(5L))).isEqualTo(5L);
        assertThat(PlannerDraftService.pickLodging(json("{\"lodgingId\":6}"), Set.of(5L))).isNull();
        assertThat(PlannerDraftService.pickLodging(json("{\"lodgingId\":null}"), Set.of(5L))).isNull();
    }

    @Test
    void 숙소는_마지막_날을_빼고_날마다_맨_끝에_둔다() {
        List<PlannerScheduleSlotEntity> rows = PlannerDraftService.toEntities(
                10L, List.of(D1, D2), List.of(Arrays.asList(1L, null), List.of(2L, 3L)), 5L);

        assertThat(rows)
                .extracting(PlannerScheduleSlotEntity::getVisitDate,
                        PlannerScheduleSlotEntity::getSortOrder,
                        PlannerScheduleSlotEntity::getTourPlaceId)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple(D1, 1, 1L),
                        org.assertj.core.groups.Tuple.tuple(D1, 2, null),
                        org.assertj.core.groups.Tuple.tuple(D1, 3, 5L),
                        org.assertj.core.groups.Tuple.tuple(D2, 1, 2L),
                        org.assertj.core.groups.Tuple.tuple(D2, 2, 3L));
    }

    @Test
    void 일정_밀도_블록을_하루_칸_수로_읽는다() {
        assertThat(PlannerDraftService.slotsPerDay("하루 1~2곳", 3)).isEqualTo(2);
        assertThat(PlannerDraftService.slotsPerDay("하루 3~4곳", 3)).isEqualTo(4);
        assertThat(PlannerDraftService.slotsPerDay("직접 지정", 3)).isEqualTo(3);
        assertThat(PlannerDraftService.slotsPerDay(null, 2)).isEqualTo(2);
        assertThat(PlannerDraftService.slotsPerDay("하루 20곳", 3)).isEqualTo(PlannerDraftService.MAX_PER_DAY);
    }

    @Test
    void 장소_이름은_띄어쓰기를_무시하고_포함으로_맞춘다() {
        assertThat(PlannerDraftService.matchesAny("경포 해수욕장", List.of("경포해수욕장"))).isTrue();
        assertThat(PlannerDraftService.matchesAny("오죽헌", List.of("오죽헌 시립박물관"))).isTrue();
        // 한 글자는 거의 모든 이름에 들어가서 무시한다
        assertThat(PlannerDraftService.matchesAny("경포대", List.of("경"))).isFalse();
    }

    // ── 전체 흐름 ────────────────────────────────────────────

    private GeneratePlannerRequestDto request() {
        GeneratePlannerRequestDto dto = new GeneratePlannerRequestDto();
        dto.setTitle("강릉 여행");
        dto.setMemberCount(2);
        dto.setIsSolo(false);
        dto.setRegionCode("51");
        dto.setCityName("강릉");
        dto.setStartDate(D1);
        dto.setEndDate(D1);
        GeneratePlannerRequestDto.Block block = new GeneratePlannerRequestDto.Block();
        block.setType(PlannerBlockType.WALKING);
        block.setValue("걷기 최소화");
        dto.setBlocks(List.of(block));
        return dto;
    }

    private TourPlaceEntity place(long id) {
        TourPlaceEntity place = new TourPlaceEntity();
        place.setTourPlaceId(id);
        place.setPlaceName("장소" + id);
        place.setCategory(TourPlaceCategory.ATTRACTION);
        return place;
    }

    private void givenCandidates() {
        given(tourPlaceRepository.findByCountryNameAndCityName("대한민국", "강릉"))
                .willReturn(List.of(place(1L)));
        given(travelPreferenceService.getPreference("user"))
                .willReturn(new TravelPreferenceResponseDto(PreferredTransport.PUBLIC_TRANSIT, 3, false));
    }

    @Test
    void AI가_실패하면_플래너를_만들지_않는다() {
        givenCandidates();
        given(openAiClient.completeJson(anyString(), anyString()))
                .willThrow(new AiUnavailableException("실패"));

        assertThatThrownBy(() -> service.generate(request(), "user"))
                .isInstanceOf(AiUnavailableException.class);
        verify(transactionTemplate, never()).execute(any());
        verify(plannerService, never()).createPlanner(any(), anyString());
    }

    @Test
    void 블록과_기본_설정을_프롬프트에_넣는다() {
        givenCandidates();
        given(openAiClient.completeJson(anyString(), anyString()))
                .willThrow(new AiUnavailableException("실패"));

        assertThatThrownBy(() -> service.generate(request(), "user"));

        ArgumentCaptor<String> prompt = ArgumentCaptor.forClass(String.class);
        verify(openAiClient).completeJson(anyString(), prompt.capture());
        assertThat(prompt.getValue())
                .contains("걷기 부담: 걷기 최소화")
                .contains("계단 이용: 어려움")
                .contains("1|장소1|명소|")
                // 하루짜리라 숙소가 필요 없다
                .contains("lodgingId 는 null");
    }

    @Test
    void 후보가_없으면_AI를_부르지_않는다() {
        given(tourPlaceRepository.findByCountryNameAndCityName("대한민국", "강릉")).willReturn(List.of());

        assertThatThrownBy(() -> service.generate(request(), "user"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("강릉");
        verify(openAiClient, never()).completeJson(anyString(), anyString());
    }

    @Test
    void 없는_시도_코드면_AI를_부르지_않는다() {
        GeneratePlannerRequestDto dto = request();
        dto.setRegionCode("99");

        assertThatThrownBy(() -> service.generate(dto, "user"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("시·도 코드");
        verify(openAiClient, never()).completeJson(anyString(), anyString());
    }

    @Test
    void 기간이_너무_길면_거부한다() {
        GeneratePlannerRequestDto dto = request();
        dto.setEndDate(D1.plusDays(PlannerDraftService.MAX_DAYS));

        assertThatThrownBy(() -> service.generate(dto, "user"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("14일");
    }

    @Test
    void 필수_장소가_칸_수보다_많으면_AI를_부르지_않는다() {
        GeneratePlannerRequestDto dto = request();
        GeneratePlannerRequestDto.Block density = new GeneratePlannerRequestDto.Block();
        density.setType(PlannerBlockType.DAILY_DENSITY);
        density.setValue("하루 1곳");
        GeneratePlannerRequestDto.Block must = new GeneratePlannerRequestDto.Block();
        must.setType(PlannerBlockType.MUST_INCLUDE);
        must.setValue("장소");
        dto.setBlocks(List.of(density, must));
        given(tourPlaceRepository.findByCountryNameAndCityName("대한민국", "강릉"))
                .willReturn(List.of(place(1L), place(2L)));
        given(travelPreferenceService.getPreference("user"))
                .willReturn(new TravelPreferenceResponseDto(PreferredTransport.PUBLIC_TRANSIT, 3, true));

        assertThatThrownBy(() -> service.generate(dto, "user"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("반드시 포함할 곳이 2곳");
        verify(openAiClient, never()).completeJson(anyString(), anyString());
    }

    @Test
    void 평점이_낮은_필수_장소와_숙소도_후보에서_빠지지_않는다() {
        GeneratePlannerRequestDto dto = request();
        dto.setEndDate(D2);
        GeneratePlannerRequestDto.Block must = new GeneratePlannerRequestDto.Block();
        must.setType(PlannerBlockType.MUST_INCLUDE);
        must.setValue("할머니댁");
        dto.setBlocks(List.of(must));
        List<TourPlaceEntity> places = new java.util.ArrayList<>();
        for (long id = 1; id <= PlannerDraftService.MAX_CANDIDATES + 10; id++) {
            TourPlaceEntity p = place(id);
            p.setRating(4.5);
            places.add(p);
        }
        TourPlaceEntity grandma = place(900L);
        grandma.setPlaceName("할머니댁");
        grandma.setRating(1.0);
        TourPlaceEntity lodging = place(901L);
        lodging.setCategory(TourPlaceCategory.ACCOMMODATION);
        lodging.setRating(1.0);
        places.add(grandma);
        places.add(lodging);
        given(tourPlaceRepository.findByCountryNameAndCityName("대한민국", "강릉")).willReturn(places);
        given(travelPreferenceService.getPreference("user"))
                .willReturn(new TravelPreferenceResponseDto(PreferredTransport.PUBLIC_TRANSIT, 3, true));
        given(openAiClient.completeJson(anyString(), anyString()))
                .willThrow(new AiUnavailableException("실패"));

        assertThatThrownBy(() -> service.generate(dto, "user"));

        ArgumentCaptor<String> prompt = ArgumentCaptor.forClass(String.class);
        verify(openAiClient).completeJson(anyString(), prompt.capture());
        assertThat(prompt.getValue()).contains("900|할머니댁|").contains("901|장소901|");
    }
}
