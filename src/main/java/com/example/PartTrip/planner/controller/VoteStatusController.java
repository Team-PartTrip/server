package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.VoteStatusResponseDto;
import com.example.PartTrip.planner.service.VoteStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class VoteStatusController {

    private final VoteStatusService voteStatusService;

    @GetMapping("/{plannerId}/votes")
    public ResponseEntity<List<VoteStatusResponseDto>> getVotes(
            Authentication authentication,
            @PathVariable Long plannerId
    ) {
        return ResponseEntity.ok(
                voteStatusService.getVotes(plannerId, authentication.getName())
        );
    }

    @GetMapping("/{plannerId}/votes/{voteId}")
    public ResponseEntity<VoteStatusResponseDto> getVote(
            Authentication authentication,
            @PathVariable Long plannerId,
            @PathVariable Long voteId
    ) {
        return ResponseEntity.ok(
                voteStatusService.getVote(plannerId, voteId, authentication.getName())
        );
    }
}
