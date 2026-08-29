package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PlannerInvitationResponseDto {
    private Long invitationId;
    private Long plannerId;
    private String plannerTitle;
    private String invitedUserId;
    private String invitedByUserId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;
}
