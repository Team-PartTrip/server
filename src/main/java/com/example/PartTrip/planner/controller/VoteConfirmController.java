package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.request.VoteConfirmRequestDto;
import com.example.PartTrip.planner.dto.response.VoteCloseResponseDto;
import com.example.PartTrip.planner.dto.response.VoteConfirmResponseDto;
import com.example.PartTrip.planner.service.VoteConfirmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class VoteConfirmController {

    private final VoteConfirmService voteConfirmService;

    @PostMapping("/{plannerId}/votes/{voteId}/close")
    public ResponseEntity<VoteCloseResponseDto> closeVote(
            Authentication authentication,
            @PathVariable Long plannerId,
            @PathVariable Long voteId
    ) {
        return ResponseEntity.ok(
                voteConfirmService.closeVote(
                        plannerId,
                        voteId,
                        authentication.getName()
                )
        );
    }

    @PostMapping("/{plannerId}/votes/{voteId}/confirm")
    public ResponseEntity<VoteConfirmResponseDto> confirmVote(
            Authentication authentication,
            @PathVariable Long plannerId,
            @PathVariable Long voteId,
            @RequestBody(required = false) VoteConfirmRequestDto requestDto
    ) {
        VoteConfirmRequestDto dto = requestDto == null
                ? new VoteConfirmRequestDto()
                : requestDto;

        return ResponseEntity.ok(
                voteConfirmService.confirmVote(
                        plannerId,
                        voteId,
                        dto,
                        authentication.getName()
                )
        );
    }
}
