package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PlannerCreateResponseDto {

    private Long plannerId;

    private String title;

    private String status;

    private Integer memberCount;

    private LocalDate startDate;

    private LocalDate endDate;

    private String countryName;

    private String cityName;

    private String inviteLink;
}
