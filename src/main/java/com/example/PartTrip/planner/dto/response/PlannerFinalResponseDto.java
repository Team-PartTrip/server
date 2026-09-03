package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class PlannerFinalResponseDto {

    private Long plannerId;
    private String title;
    private String countryName;
    private String cityName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private List<ConfirmedPlaceResponseDto> places;
}
