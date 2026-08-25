package com.example.PartTrip.planner.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PlannerMemberResponseDto {

    private String userId;
    private String nickName;
    private String role;
    private LocalDateTime joinedAt;
}
