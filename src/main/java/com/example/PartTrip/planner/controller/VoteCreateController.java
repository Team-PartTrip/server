package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.CreateVoteRequestDto;
import com.example.PartTrip.planner.dto.VoteCreateResponseDto;
import com.example.PartTrip.planner.service.VoteCreateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
public class VoteCreateController {

    private final VoteCreateService voteCreateService;

    @PostMapping("/{plannerId}/votes")
    public ResponseEntity<VoteCreateResponseDto> createVote(
            Authentication authentication,
            @PathVariable Long plannerId,
            @Valid @RequestBody CreateVoteRequestDto requestDto
    ) {
        VoteCreateResponseDto response = voteCreateService.createVote(
                plannerId,
                requestDto,
                authentication.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
