package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.response.ConfirmedPlaceResponseDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.PlannerCityEntity;
import com.example.PartTrip.planner.repository.PlannerCityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PlannerScheduleServiceTest {

    @Mock private PlannerCityRepository plannerCityRepository;
    @Mock private TourPlaceRepository tourPlaceRepository;
    @InjectMocks private PlannerScheduleService plannerScheduleService;

    @Test
    void 다중_도시의_날짜_구간을_섞지_않는다() {
        GroupTravelPlanEntity plan = plan();
        PlannerCityEntity osaka = city("일본", "오사카",
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 2));
        PlannerCityEntity kyoto = city("일본", "교토",
                LocalDate.of(2026, 10, 3), LocalDate.of(2026, 10, 4));
        TourPlaceEntity osakaPlace = place(1L, "일본", "오사카", "오사카성", 4.5, 34.6873, 135.5262);
        TourPlaceEntity kyotoPlace = place(2L, "일본", "교토", "청수사", 4.7, 34.9949, 135.7850);

        given(plannerCityRepository.findByPlanIdOrderBySeqAsc(10L))
                .willReturn(List.of(osaka, kyoto));
        given(tourPlaceRepository.findAllById(any()))
                .willReturn(List.of(osakaPlace, kyotoPlace));
        given(tourPlaceRepository.search("일본", "오사카", null)).willReturn(List.of(osakaPlace));
        given(tourPlaceRepository.search("일본", "교토", null)).willReturn(List.of(kyotoPlace));

        List<PlannerScheduleService.ScheduledPlace> result = plannerScheduleService
                .buildSchedule(plan, List.of(confirmed(1L), confirmed(2L)));

        assertThat(result).filteredOn(item -> item.tourPlace().getCityName().equals("오사카"))
                .allMatch(item -> !item.date().isAfter(LocalDate.of(2026, 10, 2)));
        assertThat(result).filteredOn(item -> item.tourPlace().getCityName().equals("교토"))
                .allMatch(item -> !item.date().isBefore(LocalDate.of(2026, 10, 3)));
    }

    @Test
    void 장소가_부족하면_같은_도시의_평점_높은_장소로_채운다() {
        GroupTravelPlanEntity plan = plan();
        TourPlaceEntity confirmed = place(1L, "일본", "오사카", "오사카성", 4.3, 34.68, 135.52);
        TourPlaceEntity first = place(2L, "일본", "오사카", "도톤보리", 4.9, 34.67, 135.50);
        TourPlaceEntity second = place(3L, "일본", "오사카", "우메다", 4.8, 34.70, 135.49);

        given(plannerCityRepository.findByPlanIdOrderBySeqAsc(10L)).willReturn(List.of());
        given(tourPlaceRepository.findAllById(any())).willReturn(List.of(confirmed));
        given(tourPlaceRepository.search("일본", "오사카", null))
                .willReturn(List.of(first, second, confirmed));

        List<PlannerScheduleService.ScheduledPlace> result = plannerScheduleService
                .buildSchedule(plan, List.of(confirmed(1L)));

        assertThat(result).extracting(item -> item.tourPlace().getTourPlaceId())
                .containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    void 가까운_장소끼리_같은_날에_묶고_하루_안에서는_평점순이다() {
        GroupTravelPlanEntity plan = plan();
        TourPlaceEntity a = place(1L, "일본", "오사카", "A", 4.0, 34.00, 135.00);
        TourPlaceEntity b = place(2L, "일본", "오사카", "B", 4.9, 34.01, 135.01);
        TourPlaceEntity c = place(3L, "일본", "오사카", "C", 4.2, 35.00, 136.00);
        TourPlaceEntity d = place(4L, "일본", "오사카", "D", 4.8, 35.01, 136.01);
        TourPlaceEntity e = place(5L, "일본", "오사카", "E", 4.1, 35.02, 136.02);
        TourPlaceEntity f = place(6L, "일본", "오사카", "F", 4.7, 34.02, 135.02);
        List<TourPlaceEntity> all = List.of(a, b, c, d, e, f);

        given(plannerCityRepository.findByPlanIdOrderBySeqAsc(10L)).willReturn(List.of());
        given(tourPlaceRepository.findAllById(any())).willReturn(all);

        List<PlannerScheduleService.ScheduledPlace> result = plannerScheduleService
                .buildSchedule(plan, all.stream().map(p -> confirmed(p.getTourPlaceId())).toList());

        assertThat(result).filteredOn(item -> item.date().equals(LocalDate.of(2026, 10, 1)))
                .extracting(item -> item.tourPlace().getTourPlaceId())
                .containsExactlyInAnyOrder(1L, 2L, 6L);
        assertThat(result).filteredOn(item -> item.date().equals(LocalDate.of(2026, 10, 2)))
                .extracting(item -> item.tourPlace().getTourPlaceId())
                .containsExactlyInAnyOrder(3L, 4L, 5L);
        assertThat(result).filteredOn(item -> item.date().equals(LocalDate.of(2026, 10, 1)))
                .extracting(item -> item.tourPlace().getRating())
                .isSortedAccordingTo(java.util.Comparator.reverseOrder());
        assertThat(result).filteredOn(item -> item.date().equals(LocalDate.of(2026, 10, 2)))
                .extracting(item -> item.tourPlace().getRating())
                .isSortedAccordingTo(java.util.Comparator.reverseOrder());
    }

    @Test
    void 숙소가_여러_곳이어도_평점이_가장_높은_한_곳을_매일_사용한다() {
        GroupTravelPlanEntity plan = plan();
        TourPlaceEntity first = place(1L, "일본", "오사카", "호텔 A", 4.9, 34.68, 135.52);
        first.setCategory(TourPlaceCategory.ACCOMMODATION);
        TourPlaceEntity second = place(2L, "일본", "오사카", "호텔 B", 4.5, 34.69, 135.53);
        second.setCategory(TourPlaceCategory.ACCOMMODATION);

        given(plannerCityRepository.findByPlanIdOrderBySeqAsc(10L)).willReturn(List.of());
        given(tourPlaceRepository.findAllById(any())).willReturn(List.of(first, second));
        given(tourPlaceRepository.search("일본", "오사카", null))
                .willReturn(List.of(first, second));

        List<PlannerScheduleService.ScheduledPlace> result = plannerScheduleService
                .buildSchedule(plan, List.of(confirmed(1L), confirmed(2L)));

        assertThat(result).filteredOn(item ->
                        item.tourPlace().getCategory() == TourPlaceCategory.ACCOMMODATION)
                .extracting(item -> item.tourPlace().getTourPlaceId())
                .containsExactly(1L, 1L);
    }

    @Test
    void 같은_도시_구간이_반복되어도_확정_장소를_중복_배치하지_않는다() {
        GroupTravelPlanEntity plan = plan();
        PlannerCityEntity firstSegment = city("일본", "오사카",
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 1));
        PlannerCityEntity secondSegment = city("일본", "오사카",
                LocalDate.of(2026, 10, 2), LocalDate.of(2026, 10, 2));
        TourPlaceEntity confirmed = place(1L, "일본", "오사카", "오사카성", 4.8, 34.68, 135.52);

        given(plannerCityRepository.findByPlanIdOrderBySeqAsc(10L))
                .willReturn(List.of(firstSegment, secondSegment));
        given(tourPlaceRepository.findAllById(any())).willReturn(List.of(confirmed));
        given(tourPlaceRepository.search("일본", "오사카", null)).willReturn(List.of(confirmed));

        List<PlannerScheduleService.ScheduledPlace> result = plannerScheduleService
                .buildSchedule(plan, List.of(confirmed(1L)));

        assertThat(result).extracting(item -> item.tourPlace().getTourPlaceId())
                .containsOnlyOnce(1L);
    }

    private GroupTravelPlanEntity plan() {
        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setPlanId(10L);
        plan.setCountryName("일본");
        plan.setCityName("오사카");
        plan.setStartDate(LocalDate.of(2026, 10, 1));
        plan.setEndDate(LocalDate.of(2026, 10, 2));
        return plan;
    }

    private PlannerCityEntity city(String country, String city, LocalDate start, LocalDate end) {
        PlannerCityEntity entity = new PlannerCityEntity();
        entity.setCountryName(country);
        entity.setCityName(city);
        entity.setStartDate(start);
        entity.setEndDate(end);
        return entity;
    }

    private TourPlaceEntity place(Long id, String country, String city, String name,
                                  double rating, double latitude, double longitude) {
        TourPlaceEntity place = new TourPlaceEntity();
        place.setTourPlaceId(id);
        place.setCountryName(country);
        place.setCityName(city);
        place.setPlaceName(name);
        place.setCategory(TourPlaceCategory.ATTRACTION);
        place.setRating(rating);
        place.setLatitude(latitude);
        place.setLongitude(longitude);
        return place;
    }

    private ConfirmedPlaceResponseDto confirmed(Long id) {
        return ConfirmedPlaceResponseDto.builder().tourPlaceId(id).build();
    }
}
