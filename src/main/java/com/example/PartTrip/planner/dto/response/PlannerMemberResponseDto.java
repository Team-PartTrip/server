package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PlannerMemberResponseDto {

    private Long invitationId;
    private String userId;
    private String nickName;
    private String role;
    private String status;
    private LocalDateTime joinedAt;
}
