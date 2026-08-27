package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.CreateVoteOptionRequestDto;
import com.example.PartTrip.planner.dto.VoteOptionResponseDto;
import com.example.PartTrip.planner.service.VoteOptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class VoteOptionController {

    private final VoteOptionService voteOptionService;

    @PostMapping("/{plannerId}/votes/{voteId}/options")
    public ResponseEntity<VoteOptionResponseDto> addOption(
            Authentication authentication,
            @PathVariable Long plannerId,
            @PathVariable Long voteId,
            @Valid @RequestBody CreateVoteOptionRequestDto requestDto
    ) {
        VoteOptionResponseDto response = voteOptionService.addOption(
                plannerId,
                voteId,
                requestDto,
                authentication.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{plannerId}/votes/{voteId}/options/{optionId}")
    public ResponseEntity<Void> deleteOption(
            Authentication authentication,
            @PathVariable Long plannerId,
            @PathVariable Long voteId,
            @PathVariable Long optionId
    ) {
        voteOptionService.deleteOption(
                plannerId,
                voteId,
                optionId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}
