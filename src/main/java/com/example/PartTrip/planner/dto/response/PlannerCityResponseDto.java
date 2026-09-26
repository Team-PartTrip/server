package com.example.PartTrip.planner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/** 플래너가 도는 도시 한 곳과 머무는 기간 */
@Getter
@AllArgsConstructor
public class PlannerCityResponseDto {

    private String regionCode;
    private String regionName;
    private String cityName;
    private LocalDate startDate;
    private LocalDate endDate;
}
