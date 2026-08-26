package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PlannerTravelPlanResponseDto {

    private Long plannerId;
    private Long planId;
    private String title;
    private String countryName;
    private String cityName;
    private LocalDate startDate;
    private LocalDate endDate;
}
