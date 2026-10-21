package com.example.PartTrip.planner.service;

import com.example.PartTrip.global.exception.AiUnavailableException;
import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.entity.TourPlaceAccessibilityEntity;
import com.example.PartTrip.main.repository.TourPlaceAccessibilityRepository;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.main.service.AccessibilityService;
import com.example.PartTrip.main.service.TourPlaceImportService;
import com.example.PartTrip.planner.dto.request.GeneratePlannerRequestDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.PlannerScheduleSlotEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.enums.PlannerBlockType;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.PlannerScheduleSlotRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.profile.dto.TravelPreferenceResponseDto;
import com.example.PartTrip.profile.enums.AvoidFood;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class PlannerDraftServiceTest {

    private static final ObjectMapper JSON = new ObjectMapper();
    private static final LocalDate D1 = LocalDate.of(2026, 10, 10);
    private static final LocalDate D2 = LocalDate.of(2026, 10, 11);

    @Mock private TourPlaceImportService tourPlaceImportService;
    @Mock private TourPlaceRepository tourPlaceRepository;
    @Mock private TourPlaceAccessibilityRepository accessibilityRepository;
    @Mock private AccessibilityService accessibilityService;
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

    private TourPlaceEntity named(long id, String name, Double rating) {
        TourPlaceEntity p = place(id);
        p.setPlaceName(name);
        p.setRating(rating);
        return p;
    }

    @Test
    void 이름이_정확히_같은_장소를_먼저_찾고_띄어쓰기는_무시한다() {
        List<TourPlaceEntity> places = List.of(
                named(1L, "경포 해수욕장", 4.5), named(2L, "경포해수욕장 주차장", 4.9));

        assertThat(PlannerDraftService.resolve(places, List.of("경포해수욕장")))
                .extracting(TourPlaceEntity::getTourPlaceId).containsExactly(1L);
    }

    @Test
    void 짧게_적으면_포함하는_장소_중_평점이_가장_높은_한_곳만_고른다() {
        List<TourPlaceEntity> places = List.of(
                named(1L, "강릉중앙시장", 4.3), named(2L, "주문진수산시장", 4.6),
                named(3L, "강릉성남시장", 4.1), named(4L, "경포대", 4.8));

        assertThat(PlannerDraftService.resolve(places, List.of("시장")))
                .extracting(TourPlaceEntity::getTourPlaceId).containsExactly(2L);
    }

    @Test
    void 적은_이름이_더_길어도_장소_이름을_포함하면_찾는다() {
        List<TourPlaceEntity> places = List.of(named(1L, "오죽헌", 4.4), named(2L, "경포대", 4.8));

        assertThat(PlannerDraftService.resolve(places, List.of("오죽헌 시립박물관")))
                .extracting(TourPlaceEntity::getTourPlaceId).containsExactly(1L);
    }

    @Test
    void 한_글자와_없는_이름은_찾지_않고_여러_이름은_각각_찾는다() {
        List<TourPlaceEntity> places = List.of(named(1L, "오죽헌", 4.4), named(2L, "경포대", 4.8));

        assertThat(PlannerDraftService.resolve(places, List.of("경"))).isEmpty();
        assertThat(PlannerDraftService.resolve(places, List.of("할머니댁"))).isEmpty();
        assertThat(PlannerDraftService.resolve(places, List.of("오죽헌", "경포대")))
                .extracting(TourPlaceEntity::getTourPlaceId).containsExactly(1L, 2L);
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

    @Test
    void 일정_장소에_설명과_세부_종류를_담는다() {
        TourPlaceEntity place = place(1);
        place.setDescription("노포 국수집");
        place.setPrimaryTypeLabel("국수 전문점");

        var response = PlannerDraftService.toPlace(place);

        assertThat(response.description()).isEqualTo("노포 국수집");
        assertThat(response.typeLabel()).isEqualTo("국수 전문점");
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
    void 피하는_음식을_프롬프트에_넣는다() {
        givenCandidates();
        given(travelPreferenceService.getPreference("user"))
                .willReturn(new TravelPreferenceResponseDto(PreferredTransport.PUBLIC_TRANSIT, 3, true,
                        null, List.of(AvoidFood.SEAFOOD, AvoidFood.HARD), null, null));
        given(openAiClient.completeJson(anyString(), anyString()))
                .willThrow(new AiUnavailableException("실패"));

        assertThatThrownBy(() -> service.generate(request(), "user"));

        ArgumentCaptor<String> prompt = ArgumentCaptor.forClass(String.class);
        verify(openAiClient).completeJson(anyString(), prompt.capture());
        assertThat(prompt.getValue()).contains("피하는 음식: 해산물, 딱딱하거나 질긴 음식");
    }

    @Test
    void 후보_장소에_업종을_같이_보낸다() {
        TourPlaceEntity noodle = place(1L);
        noodle.setCategory(TourPlaceCategory.RESTAURANT);
        noodle.setPrimaryTypeLabel("한식당");
        given(tourPlaceRepository.findByCountryNameAndCityName("대한민국", "강릉"))
                .willReturn(List.of(noodle));
        given(travelPreferenceService.getPreference("user"))
                .willReturn(new TravelPreferenceResponseDto(PreferredTransport.PUBLIC_TRANSIT, 3, true));
        given(openAiClient.completeJson(anyString(), anyString()))
                .willThrow(new AiUnavailableException("실패"));

        assertThatThrownBy(() -> service.generate(request(), "user"));

        ArgumentCaptor<String> prompt = ArgumentCaptor.forClass(String.class);
        verify(openAiClient).completeJson(anyString(), prompt.capture());
        assertThat(prompt.getValue())
                .contains("(id|이름|분류|업종|위도,경도|평점|무장애)")
                .contains("1|장소1|맛집|한식당|null,null||");
    }

    @Test
    void 휠체어와_오래_걷기를_프롬프트에_넣고_무장애_장소를_표시한다() {
        givenCandidates();
        given(travelPreferenceService.getPreference("user"))
                .willReturn(new TravelPreferenceResponseDto(PreferredTransport.PUBLIC_TRANSIT, 3, false,
                        null, null, true, true));
        TourPlaceAccessibilityEntity row = new TourPlaceAccessibilityEntity();
        row.setTourPlaceId(1L);
        row.setContentId("126508");
        row.setItemsJson("[{\"key\":\"ROUTE\",\"label\":\"접근로\",\"text\":\"경사로 있음\"}]");
        given(accessibilityRepository.findAllById(List.of(1L))).willReturn(List.of(row));
        given(openAiClient.completeJson(anyString(), anyString()))
                .willThrow(new AiUnavailableException("실패"));

        assertThatThrownBy(() -> service.generate(request(), "user"));

        ArgumentCaptor<String> prompt = ArgumentCaptor.forClass(String.class);
        verify(openAiClient).completeJson(anyString(), prompt.capture());
        assertThat(prompt.getValue())
                .contains("휠체어 이용: 예")
                .contains("오래 걷기: 어려움")
                .contains("1|장소1|명소||null,null||무장애");
    }

    @Test
    void 이동이_불편하면_무장애가_아닌_액티비티를_후보에서_뺀다() {
        TourPlaceEntity museum = place(1L);
        TourPlaceEntity laserTag = place(2L);
        laserTag.setCategory(TourPlaceCategory.ACTIVITY);
        TourPlaceEntity rampedActivity = place(3L);
        rampedActivity.setCategory(TourPlaceCategory.ACTIVITY);
        given(tourPlaceRepository.findByCountryNameAndCityName("대한민국", "강릉"))
                .willReturn(List.of(museum, laserTag, rampedActivity));
        given(travelPreferenceService.getPreference("user"))
                .willReturn(new TravelPreferenceResponseDto(PreferredTransport.PUBLIC_TRANSIT, 3, true,
                        null, null, null, true));
        TourPlaceAccessibilityEntity row = new TourPlaceAccessibilityEntity();
        row.setTourPlaceId(3L);
        row.setContentId("1");
        row.setItemsJson("[{\"key\":\"ROUTE\",\"label\":\"접근로\",\"text\":\"경사로\"}]");
        given(accessibilityRepository.findAllById(any())).willReturn(List.of(row));
        given(openAiClient.completeJson(anyString(), anyString()))
                .willThrow(new AiUnavailableException("실패"));

        assertThatThrownBy(() -> service.generate(request(), "user"));

        ArgumentCaptor<String> prompt = ArgumentCaptor.forClass(String.class);
        verify(openAiClient).completeJson(anyString(), prompt.capture());
        assertThat(prompt.getValue())
                .contains("1|장소1|")
                .doesNotContain("2|장소2|")
                .contains("3|장소3|");
    }

    private void givenLimitedWalking() {
        given(travelPreferenceService.getPreference("user"))
                .willReturn(new TravelPreferenceResponseDto(PreferredTransport.PUBLIC_TRANSIT, 3, true,
                        null, null, null, true));
        given(openAiClient.completeJson(anyString(), anyString()))
                .willThrow(new AiUnavailableException("실패"));
    }

    @Test
    void 이동이_불편하면_무장애_정보를_조회한_적_없는_장소는_미리_받아서_판단한다() {
        TourPlaceEntity museum = place(1L);
        TourPlaceEntity yacht = place(2L);
        yacht.setCategory(TourPlaceCategory.ACTIVITY);
        TourPlaceEntity pottery = place(3L);
        pottery.setCategory(TourPlaceCategory.ACTIVITY);
        given(tourPlaceRepository.findByCountryNameAndCityName("대한민국", "강릉"))
                .willReturn(List.of(museum, yacht, pottery));
        givenLimitedWalking();
        TourPlaceAccessibilityEntity fetched = new TourPlaceAccessibilityEntity();
        fetched.setTourPlaceId(3L);
        fetched.setContentId("1");
        fetched.setItemsJson("[{\"key\":\"ROUTE\",\"label\":\"접근로\",\"text\":\"경사로\"}]");
        given(accessibilityRepository.findAllById(any()))
                .willReturn(List.of())
                .willReturn(List.of(fetched));

        assertThatThrownBy(() -> service.generate(request(), "user"));

        verify(accessibilityService).get(2L);
        verify(accessibilityService).get(3L);
        ArgumentCaptor<String> prompt = ArgumentCaptor.forClass(String.class);
        verify(openAiClient).completeJson(anyString(), prompt.capture());
        assertThat(prompt.getValue()).doesNotContain("2|장소2|").contains("3|장소3|");
    }

    @Test
    void 이동이_불편하지_않으면_무장애_정보를_미리_받지_않는다() {
        givenCandidates();
        given(travelPreferenceService.getPreference("user"))
                .willReturn(new TravelPreferenceResponseDto(PreferredTransport.PUBLIC_TRANSIT, 3, true));
        given(openAiClient.completeJson(anyString(), anyString()))
                .willThrow(new AiUnavailableException("실패"));

        assertThatThrownBy(() -> service.generate(request(), "user"));

        verifyNoInteractions(accessibilityService);
    }

    @Test
    void 미리_받는_장소는_상한까지만이고_반드시_포함할_곳이_먼저다() {
        GeneratePlannerRequestDto dto = request();
        GeneratePlannerRequestDto.Block must = new GeneratePlannerRequestDto.Block();
        must.setType(PlannerBlockType.MUST_INCLUDE);
        must.setValue("할머니댁");
        dto.setBlocks(List.of(must));
        List<TourPlaceEntity> places = new java.util.ArrayList<>();
        for (long id = 1; id <= 60; id++) {
            TourPlaceEntity p = place(id);
            p.setRating(4.5);
            places.add(p);
        }
        TourPlaceEntity grandma = place(900L);
        grandma.setPlaceName("할머니댁");
        grandma.setRating(1.0);
        places.add(grandma);
        given(tourPlaceRepository.findByCountryNameAndCityName("대한민국", "강릉")).willReturn(places);
        givenLimitedWalking();

        assertThatThrownBy(() -> service.generate(dto, "user"));

        verify(accessibilityService).get(900L);
        verify(accessibilityService, org.mockito.Mockito.times(PlannerDraftService.ACCESSIBILITY_PREFETCH_LIMIT))
                .get(anyLong());
    }

    @Test
    void 직접_넣은_곳에_무장애_정보가_없으면_경고를_담는다() {
        TourPlaceEntity ramped = named(1L, "오죽헌", 4.4);
        TourPlaceEntity unknown = named(2L, "경포대", 4.8);

        var warnings = PlannerDraftService.missingAccessibilityWarnings(
                List.of(ramped, unknown), Set.of(1L));

        assertThat(warnings).hasSize(1);
        assertThat(warnings.get(0).tourPlaceId()).isEqualTo(2L);
        assertThat(warnings.get(0).message()).contains("무장애 정보가 없는 곳이에요").contains("경포대");
        assertThat(PlannerDraftService.missingAccessibilityWarnings(List.of(ramped), Set.of(1L))).isEmpty();
    }

    @Test
    void 응답에_경고를_붙여도_나머지는_그대로이고_조회_응답은_경고가_비어_있다() {
        var plain = new com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto(
                1L, "여행", "강릉", D1, D1, List.of());
        var warned = plain.withWarnings(List.of(
                new com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto.Warning(2L, "경고")));

        assertThat(plain.warnings()).isEmpty();
        assertThat(warned.warnings()).hasSize(1);
        assertThat(warned.plannerId()).isEqualTo(1L);
        assertThat(warned.cityName()).isEqualTo("강릉");
    }

    @Test
    void 하루_맛집이_넘치면_가까운_다른_후보로_바꾼다() {
        TourPlaceEntity meal1 = located(1L, TourPlaceCategory.RESTAURANT, 35.0);
        TourPlaceEntity meal2 = located(2L, TourPlaceCategory.RESTAURANT, 35.1);
        TourPlaceEntity meal3 = located(3L, TourPlaceCategory.RESTAURANT, 35.2);
        TourPlaceEntity shop = located(4L, TourPlaceCategory.SHOPPING, 35.3);
        TourPlaceEntity nearMuseum = located(5L, TourPlaceCategory.ATTRACTION, 35.21);
        TourPlaceEntity farMuseum = located(6L, TourPlaceCategory.ATTRACTION, 36.0);
        List<TourPlaceEntity> candidates = List.of(meal1, meal2, meal3, shop, nearMuseum, farMuseum);

        // 세 번째 맛집은 바로 앞(맛집2)과 가까운 명소로 바뀐다
        assertThat(PlannerDraftService.capMeals(
                List.of(Arrays.asList(1L, 4L, 2L, 3L)), 2, candidates, Set.of()))
                .containsExactly(List.of(1L, 4L, 2L, 5L));
        // 미식이면 세 곳까지 둔다
        assertThat(PlannerDraftService.capMeals(
                List.of(Arrays.asList(1L, 4L, 2L, 3L)), 3, candidates, Set.of()))
                .containsExactly(List.of(1L, 4L, 2L, 3L));
        // 반드시 포함할 맛집은 지우지 않는다
        assertThat(PlannerDraftService.capMeals(
                List.of(Arrays.asList(1L, 4L, 2L, 3L)), 2, candidates, Set.of(3L)))
                .containsExactly(List.of(1L, 4L, 2L, 3L));
        // 바꿀 후보가 없으면 빈 칸
        assertThat(PlannerDraftService.capMeals(
                List.of(Arrays.asList(1L, 2L, 3L)), 2, List.of(meal1, meal2, meal3), Set.of()))
                .containsExactly(Arrays.asList(1L, 2L, null));
    }

    private static TourPlaceEntity located(long id, TourPlaceCategory category, double latitude) {
        TourPlaceEntity place = new TourPlaceEntity();
        place.setTourPlaceId(id);
        place.setPlaceName("장소" + id);
        place.setCategory(category);
        place.setLatitude(latitude);
        place.setLongitude(128.0);
        return place;
    }

    @Test
    void 맛집끼리_카페끼리_붙으면_뒤쪽을_다른_장소와_바꾼다() {
        Map<Long, TourPlaceCategory> categories = Map.of(
                1L, TourPlaceCategory.RESTAURANT, 2L, TourPlaceCategory.RESTAURANT,
                3L, TourPlaceCategory.ATTRACTION, 4L, TourPlaceCategory.CAFE,
                5L, TourPlaceCategory.CAFE, 6L, TourPlaceCategory.SHOPPING);

        assertThat(PlannerDraftService.spreadMeals(List.of(1L, 2L, 3L), categories))
                .containsExactly(1L, 3L, 2L);
        // 뒤에 바꿀 곳이 없으면 앞쪽 빈틈으로 옮긴다
        assertThat(PlannerDraftService.spreadMeals(List.of(3L, 4L, 5L), categories))
                .containsExactly(5L, 3L, 4L);
        assertThat(PlannerDraftService.spreadMeals(List.of(3L, 6L, 1L, 2L), categories))
                .containsExactly(3L, 2L, 6L, 1L);
        // 전부 맛집이면 고칠 수 없다
        assertThat(PlannerDraftService.spreadMeals(List.of(1L, 2L), categories))
                .containsExactly(1L, 2L);
        assertThat(PlannerDraftService.spreadMeals(List.of(1L, 4L, 3L), categories))
                .containsExactly(1L, 4L, 3L);
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
        given(travelPreferenceService.getPreference("user"))
                .willReturn(new TravelPreferenceResponseDto(PreferredTransport.PUBLIC_TRANSIT, 3, true));

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
        must.setValue("장소1");
        GeneratePlannerRequestDto.Block must2 = new GeneratePlannerRequestDto.Block();
        must2.setType(PlannerBlockType.MUST_INCLUDE);
        must2.setValue("장소2");
        dto.setBlocks(List.of(density, must, must2));
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

    @Test
    void scheduleHidesRoutesUntilConfirmed() {
        TravelGroupEntity group = new TravelGroupEntity();
        group.setStatus(GroupStatus.PLANNING);
        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setPlanId(1L);
        plan.setStartDate(D1);
        plan.setEndDate(D1);
        PlannerScheduleSlotEntity slot = new PlannerScheduleSlotEntity();
        slot.setVisitDate(D1);
        slot.setSortOrder(0);
        slot.setRouteStatus("READY");
        given(travelGroupRepository.findById(7L)).willReturn(Optional.of(group));
        given(groupMemberRepository.existsByGroupIdAndUserId(7L, "u")).willReturn(true);
        given(groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(7L)).willReturn(Optional.of(plan));
        given(slotRepository.findByPlanIdOrderByVisitDateAscSortOrderAsc(1L)).willReturn(List.of(slot));

        assertThat(service.getSchedule(7L, "u").days().get(0).slots().get(0).routeStatus()).isNull();

        group.setStatus(GroupStatus.CONFIRMED);
        assertThat(service.getSchedule(7L, "u").days().get(0).slots().get(0).routeStatus()).isEqualTo("READY");
    }
}
