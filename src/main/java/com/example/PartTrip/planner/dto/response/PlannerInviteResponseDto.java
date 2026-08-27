package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlannerInviteResponseDto {

    private String inviteLink;
    private Integer invitedCount;
    private List<PlannerInvitationResponseDto> invitations;
}
