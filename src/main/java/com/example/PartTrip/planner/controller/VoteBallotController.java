package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.request.VoteBallotRequestDto;
import com.example.PartTrip.planner.dto.response.VoteBallotResponseDto;
import com.example.PartTrip.planner.service.VoteBallotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class VoteBallotController {

    private final VoteBallotService voteBallotService;

    @PutMapping("/{plannerId}/votes/{voteId}/ballot")
    public ResponseEntity<VoteBallotResponseDto> castBallot(
            Authentication authentication,
            @PathVariable Long plannerId,
            @PathVariable Long voteId,
            @Valid @RequestBody VoteBallotRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                voteBallotService.castBallot(
                        plannerId,
                        voteId,
                        requestDto,
                        authentication.getName()
                )
        );
    }
}
