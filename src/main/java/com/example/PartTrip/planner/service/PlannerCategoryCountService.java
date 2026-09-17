package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.planner.dto.request.PlannerCityRequestDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.PlannerCategoryCountEntity;
import com.example.PartTrip.planner.repository.PlannerCategoryCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 그룹장이 정한 카테고리별 확정 장소 수를 저장하고 읽는다 (Func-005-08).
 *
 * 플래너를 만들 때(PlannerService)와 여행지를 나중에 저장할 때
 * (PlannerTravelPlanService) 둘 다 여기를 지난다. PlannerCityWriter 와
 * 짝을 이뤄 도시 목록이 바뀔 때마다 개수도 같이 갈아끼운다.
 */
@Component
@RequiredArgsConstructor
public class PlannerCategoryCountService {

    // 하루에 한 카테고리를 이보다 많이 도는 여행은 일정이 되지 않는다
    private static final int MAX_COUNT_PER_CITY = 20;

    private final PlannerCategoryCountRepository plannerCategoryCountRepository;

    /**
     * 카테고리별 확정 개수를 통째로 갈아끼운다.
     *
     * 그룹장이 정하지 않은 카테고리는 예전 계산식(여행 일수 기준)을 그대로
     * 쓴다. 개수를 보내지 않는 클라이언트도 지금까지와 똑같이 동작한다.
     *
     * @param cities     도시별 개수. 비어 있으면 planCounts 로 한 줄만 쓴다
     * @param planCounts 도시를 나누지 않은 여행에서 그룹장이 정한 개수
     */
    public void replace(
            GroupTravelPlanEntity plan,
            List<PlannerCityRequestDto> cities,
            Map<TourPlaceCategory, Integer> planCounts
    ) {
        // 다시 저장할 때 이전 개수가 남으면 합이 두 배가 된다
        plannerCategoryCountRepository.deleteByPlanId(plan.getPlanId());

        List<PlannerCategoryCountEntity> rows = new ArrayList<>();

        if (cities == null || cities.isEmpty()) {
            rows.addAll(rowsOf(plan.getPlanId(), 0, planCounts,
                    days(plan.getStartDate(), plan.getEndDate())));
        } else {
            for (int seq = 0; seq < cities.size(); seq++) {
                PlannerCityRequestDto city = cities.get(seq);
                rows.addAll(rowsOf(plan.getPlanId(), seq, city.getPlaceCounts(),
                        days(city.getStartDate(), city.getEndDate())));
            }
        }

        // 숙소는 여행 내내 같은 곳이라 도시 수와 무관하게 한 곳만 확정한다
        rows.add(row(plan.getPlanId(), 0, TourPlaceCategory.ACCOMMODATION, 1));

        plannerCategoryCountRepository.saveAll(rows);
    }

    /** 이 여행에서 해당 카테고리를 몇 곳 확정할지. 도시별 개수를 모두 더한 값이다 */
    public int requiredCount(GroupTravelPlanEntity plan, TourPlaceCategory category) {
        int saved = plannerCategoryCountRepository
                .findByPlanIdOrderBySeqAsc(plan.getPlanId())
                .stream()
                .filter(row -> row.getCategory() == category)
                .mapToInt(PlannerCategoryCountEntity::getRequiredCount)
                .sum();
        if (saved > 0) {
            return saved;
        }
        // 개수를 정하는 기능이 생기기 전에 만든 플래너는 저장된 줄이 없다
        return defaultCount(category, days(plan.getStartDate(), plan.getEndDate()));
    }

    private List<PlannerCategoryCountEntity> rowsOf(
            Long planId,
            int seq,
            Map<TourPlaceCategory, Integer> counts,
            int days
    ) {
        validateAccommodation(counts);

        List<PlannerCategoryCountEntity> rows = new ArrayList<>();
        for (TourPlaceCategory category : TourPlaceCategory.values()) {
            if (category == TourPlaceCategory.ACCOMMODATION) {
                continue;
            }
            Integer requested = counts == null ? null : counts.get(category);
            rows.add(row(planId, seq, category,
                    requested == null ? defaultCount(category, days) : validate(category, requested)));
        }
        return rows;
    }

    private void validateAccommodation(Map<TourPlaceCategory, Integer> counts) {
        Integer requested = counts == null ? null : counts.get(TourPlaceCategory.ACCOMMODATION);
        if (requested != null && requested != 1) {
            throw new IllegalArgumentException("숙소는 한 곳만 확정할 수 있습니다.");
        }
    }

    private int validate(TourPlaceCategory category, int count) {
        if (count < 1 || count > MAX_COUNT_PER_CITY) {
            throw new IllegalArgumentException(category.getLabel() + " 확정 개수는 1 이상 "
                    + MAX_COUNT_PER_CITY + " 이하여야 합니다.");
        }
        return count;
    }

    private PlannerCategoryCountEntity row(
            Long planId,
            int seq,
            TourPlaceCategory category,
            int requiredCount
    ) {
        PlannerCategoryCountEntity row = new PlannerCategoryCountEntity();
        row.setPlanId(planId);
        row.setSeq(seq);
        row.setCategory(category);
        row.setRequiredCount(requiredCount);
        return row;
    }

    private int defaultCount(TourPlaceCategory category, int days) {
        return switch (category) {
            case ACCOMMODATION -> 1;
            case RESTAURANT -> days * 2;
            default -> days;
        };
    }

    private int days(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("여행 기간이 설정되지 않았습니다.");
        }
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (days <= 0 || days > Integer.MAX_VALUE / 2) {
            throw new IllegalArgumentException("여행 기간이 올바르지 않습니다.");
        }
        return Math.toIntExact(days);
    }
}
