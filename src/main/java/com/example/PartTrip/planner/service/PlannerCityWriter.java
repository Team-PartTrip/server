package com.example.PartTrip.planner.service;

import com.example.PartTrip.region.enums.RegionCode;
import com.example.PartTrip.planner.dto.request.PlannerCityRequestDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.PlannerCityEntity;
import com.example.PartTrip.planner.repository.PlannerCityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 플래너가 도는 도시들을 저장한다.
 *
 * 플래너를 만들 때(PlannerService)와 여행지를 나중에 저장할 때
 * (PlannerTravelPlanService) 둘 다 여기를 지난다. 검증이 한쪽에만 있으면
 * 다른 쪽으로 빈틈 있는 기간이 들어온다.
 */
@Component
@RequiredArgsConstructor
public class PlannerCityWriter {

    private final PlannerCityRepository plannerCityRepository;

    /**
     * 도시 목록을 통째로 갈아끼운다.
     *
     * 여행 기간을 빈틈 없이 이어 덮어야 한다. 하루라도 비면 AI 가 그날
     * 어느 도시에서 일정을 짤지 알 수 없다.
     *
     * @param cities 비어 있거나 null 이면 도시 목록을 지우기만 한다
     */
    public void replace(GroupTravelPlanEntity plan, List<PlannerCityRequestDto> cities) {
        // 다시 저장할 때 이전 도시가 남으면 기간이 두 번 덮인다
        plannerCityRepository.deleteByPlanId(plan.getPlanId());

        if (cities == null || cities.isEmpty()) {
            return;
        }

        List<PlannerCityEntity> rows = new ArrayList<>(cities.size());
        LocalDate expected = plan.getStartDate();

        for (int i = 0; i < cities.size(); i++) {
            PlannerCityRequestDto city = cities.get(i);

            if (city.getEndDate().isBefore(city.getStartDate())) {
                throw new IllegalArgumentException(
                        city.getCityName() + " 의 종료일이 시작일보다 빠릅니다.");
            }
            if (!city.getStartDate().equals(expected)) {
                throw new IllegalArgumentException(
                        "도시별 기간이 이어지지 않습니다. " + expected + " 부터 시작해야 합니다.");
            }

            PlannerCityEntity row = new PlannerCityEntity();
            row.setPlanId(plan.getPlanId());
            row.setSeq(i);
            row.setRegionCode(RegionCode.of(city.getRegionCode()).getCode());
            row.setCityName(city.getCityName().trim());
            row.setStartDate(city.getStartDate());
            row.setEndDate(city.getEndDate());
            rows.add(row);

            expected = city.getEndDate().plusDays(1);
        }

        if (!expected.minusDays(1).equals(plan.getEndDate())) {
            throw new IllegalArgumentException(
                    "도시별 기간이 여행 기간을 다 덮지 않습니다. 마지막 도시는 "
                            + plan.getEndDate() + " 에 끝나야 합니다.");
        }
        plannerCityRepository.saveAll(rows);
    }
}
