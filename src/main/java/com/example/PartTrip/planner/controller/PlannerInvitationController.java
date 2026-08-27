package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.request.InvitePlannerMembersRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerInvitationResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerInviteResponseDto;
import com.example.PartTrip.planner.service.PlannerInvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerInvitationController {

    private final PlannerInvitationService plannerInvitationService;

    @PostMapping("/{plannerId}/members")
    public ResponseEntity<PlannerInviteResponseDto> inviteMembers(
            Authentication authentication,
            @PathVariable Long plannerId,
            @Valid @RequestBody InvitePlannerMembersRequestDto requestDto
    ) {
        return ResponseEntity.ok(plannerInvitationService.inviteMembers(
                plannerId,
                requestDto,
                authentication.getName()
        ));
    }

    @GetMapping("/invitations/me")
    public ResponseEntity<List<PlannerInvitationResponseDto>> getMyInvitations(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                plannerInvitationService.getMyPendingInvitations(authentication.getName())
        );
    }

    @PostMapping("/invitations/{invitationId}/accept")
    public ResponseEntity<PlannerInvitationResponseDto> acceptInvitation(
            Authentication authentication,
            @PathVariable Long invitationId
    ) {
        return ResponseEntity.ok(
                plannerInvitationService.acceptInvitation(invitationId, authentication.getName())
        );
    }

    @PostMapping("/invitations/{invitationId}/reject")
    public ResponseEntity<PlannerInvitationResponseDto> rejectInvitation(
            Authentication authentication,
            @PathVariable Long invitationId
    ) {
        return ResponseEntity.ok(
                plannerInvitationService.rejectInvitation(invitationId, authentication.getName())
        );
    }

    @DeleteMapping("/{plannerId}/invitations/{invitationId}")
    public ResponseEntity<Void> cancelInvitation(
            Authentication authentication,
            @PathVariable Long plannerId,
            @PathVariable Long invitationId
    ) {
        plannerInvitationService.cancelInvitation(
                plannerId,
                invitationId,
                authentication.getName()
        );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{plannerId}/members/{memberUserId}")
    public ResponseEntity<Void> removeMember(
            Authentication authentication,
            @PathVariable Long plannerId,
            @PathVariable String memberUserId
    ) {
        plannerInvitationService.removeMember(
                plannerId,
                memberUserId,
                authentication.getName()
        );
        return ResponseEntity.noContent().build();
    }
}
