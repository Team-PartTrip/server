package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.planner.dto.request.PlannerCityRequestDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.PlannerCategoryCountEntity;
import com.example.PartTrip.planner.repository.PlannerCategoryCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PlannerCategoryCountServiceTest {

    private static final long PLAN_ID = 10L;

    @Mock private PlannerCategoryCountRepository plannerCategoryCountRepository;
    @Captor private ArgumentCaptor<List<PlannerCategoryCountEntity>> rowsCaptor;
    @InjectMocks private PlannerCategoryCountService service;

    private GroupTravelPlanEntity plan;

    @BeforeEach
    void setUp() {
        plan = new GroupTravelPlanEntity();
        plan.setPlanId(PLAN_ID);
        plan.setStartDate(LocalDate.of(2026, 9, 1));
        plan.setEndDate(LocalDate.of(2026, 9, 3));
    }

    @Test
    void 그룹장이_정한_개수를_저장한다() {
        service.replace(plan, null, Map.of(
                TourPlaceCategory.RESTAURANT, 6,
                TourPlaceCategory.ATTRACTION, 4));

        assertThat(savedCount(TourPlaceCategory.RESTAURANT)).isEqualTo(6);
        assertThat(savedCount(TourPlaceCategory.ATTRACTION)).isEqualTo(4);
    }

    @Test
    void 정하지_않은_카테고리는_여행_일수로_채운다() {
        service.replace(plan, null, Map.of(TourPlaceCategory.RESTAURANT, 6));

        assertThat(savedCount(TourPlaceCategory.CAFE)).isEqualTo(3);
    }

    @Test
    void 숙소는_도시가_여럿이어도_한_곳이다() {
        service.replace(plan, List.of(
                city("오사카", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 2), null),
                city("교토", LocalDate.of(2026, 9, 3), LocalDate.of(2026, 9, 3), null)), null);

        assertThat(savedCount(TourPlaceCategory.ACCOMMODATION)).isEqualTo(1);
    }

    @Test
    void 도시마다_정한_개수를_더해_확정_개수를_낸다() {
        service.replace(plan, List.of(
                city("오사카", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 2),
                        Map.of(TourPlaceCategory.RESTAURANT, 4)),
                city("교토", LocalDate.of(2026, 9, 3), LocalDate.of(2026, 9, 3),
                        Map.of(TourPlaceCategory.RESTAURANT, 2))), null);

        assertThat(savedCount(TourPlaceCategory.RESTAURANT)).isEqualTo(6);
    }

    @Test
    void 숙소를_두_곳_이상으로_정하면_거부한다() {
        Map<TourPlaceCategory, Integer> counts = Map.of(TourPlaceCategory.ACCOMMODATION, 2);

        assertThatThrownBy(() -> service.replace(plan, null, counts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("숙소는 한 곳만");
    }

    @Test
    void 개수가_범위를_벗어나면_거부한다() {
        Map<TourPlaceCategory, Integer> counts = Map.of(TourPlaceCategory.RESTAURANT, 0);

        assertThatThrownBy(() -> service.replace(plan, null, counts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("맛집");
    }

    @Test
    void 저장된_개수가_없으면_여행_일수로_계산한다() {
        given(plannerCategoryCountRepository.findByPlanIdOrderBySeqAsc(PLAN_ID))
                .willReturn(List.of());

        assertThat(service.requiredCount(plan, TourPlaceCategory.RESTAURANT)).isEqualTo(6);
        assertThat(service.requiredCount(plan, TourPlaceCategory.ACCOMMODATION)).isEqualTo(1);
    }

    /** 도시별로 저장한 줄을 더한 값. 이 여행에서 실제로 확정될 개수다 */
    private int savedCount(TourPlaceCategory category) {
        then(plannerCategoryCountRepository).should().saveAll(rowsCaptor.capture());
        return rowsCaptor.getValue().stream()
                .filter(row -> row.getCategory() == category)
                .mapToInt(PlannerCategoryCountEntity::getRequiredCount)
                .sum();
    }

    private PlannerCityRequestDto city(
            String cityName,
            LocalDate startDate,
            LocalDate endDate,
            Map<TourPlaceCategory, Integer> placeCounts
    ) {
        PlannerCityRequestDto city = new PlannerCityRequestDto();
        city.setCountryName("일본");
        city.setCityName(cityName);
        city.setStartDate(startDate);
        city.setEndDate(endDate);
        city.setPlaceCounts(placeCounts);
        return city;
    }
}
