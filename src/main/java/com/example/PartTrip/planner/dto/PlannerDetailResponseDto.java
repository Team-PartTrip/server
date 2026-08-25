package com.example.PartTrip.planner.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PlannerDetailResponseDto {

    private Long plannerId;
    private String title;
    private String countryName;
    private String cityName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String role;
    private Integer memberCount;
    private Long joinedMemberCount;
    private String inviteCode;
}
