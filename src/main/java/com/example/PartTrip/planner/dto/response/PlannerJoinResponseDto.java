package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlannerJoinResponseDto {

    private Long plannerId;

    private String title;

    private String role;

    private String status;

    private Integer memberCount;

    private Long joinedMemberCount;
}
