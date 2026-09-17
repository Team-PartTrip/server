package com.example.PartTrip.planner.dto.request;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

/** 플래너를 만들 때 그룹장이 정하는 도시 한 곳과 머무는 기간 */
@Getter
@Setter
public class PlannerCityRequestDto {

    @NotBlank(message = "나라를 입력해주세요.")
    private String countryName;

    @NotBlank(message = "도시를 입력해주세요.")
    private String cityName;

    @NotNull(message = "도시별 시작일을 입력해주세요.")
    private LocalDate startDate;

    @NotNull(message = "도시별 종료일을 입력해주세요.")
    private LocalDate endDate;

    /**
     * 이 도시에서 카테고리마다 몇 곳을 확정할지. {"RESTAURANT": 6, "CAFE": 3}
     *
     * 비워두거나 빠뜨린 카테고리는 여행 일수로 계산한 기본값을 쓴다.
     * 숙소는 여행 내내 같은 곳이라 1 만 받는다.
     */
    private Map<TourPlaceCategory, Integer> placeCounts;
}
