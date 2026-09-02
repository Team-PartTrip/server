package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.dto.CreateVoteOptionRequestDto;
import com.example.PartTrip.planner.dto.VoteOptionResponseDto;
import com.example.PartTrip.planner.dto.request.CreateVoteRequestDto;
import com.example.PartTrip.planner.dto.request.VoteBallotRequestDto;
import com.example.PartTrip.planner.dto.request.VoteConfirmRequestDto;
import com.example.PartTrip.planner.dto.response.VoteBallotResponseDto;
import com.example.PartTrip.planner.dto.response.VoteCloseResponseDto;
import com.example.PartTrip.planner.dto.response.VoteConfirmResponseDto;
import com.example.PartTrip.planner.dto.response.VoteCreateResponseDto;
import com.example.PartTrip.planner.dto.response.VoteReminderResponseDto;
import com.example.PartTrip.planner.dto.response.VoteStatusResponseDto;
import com.example.PartTrip.planner.service.VoteBallotService;
import com.example.PartTrip.planner.service.VoteConfirmService;
import com.example.PartTrip.planner.service.VoteCreateService;
import com.example.PartTrip.planner.service.VoteOptionService;
import com.example.PartTrip.planner.service.VoteReminderService;
import com.example.PartTrip.planner.service.VoteStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerVoteController {

    private final VoteCreateService voteCreateService;
    private final VoteOptionService voteOptionService;
    private final VoteBallotService voteBallotService;
    private final VoteStatusService voteStatusService;
    private final VoteConfirmService voteConfirmService;
    private final VoteReminderService voteReminderService;

    @PostMapping("/{plannerId}/votes")
    public ResponseEntity<VoteCreateResponseDto> createVote(
            Authentication authentication,
            @PathVariable Long plannerId,
            @Valid @RequestBody CreateVoteRequestDto requestDto
    ) {
        VoteCreateResponseDto response = voteCreateService.createVote(
                plannerId, requestDto, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{plannerId}/votes/{voteId}/options")
    public ResponseEntity<VoteOptionResponseDto> addOption(
            Authentication authentication,
            @PathVariable Long plannerId,
            @PathVariable Long voteId,
            @Valid @RequestBody CreateVoteOptionRequestDto requestDto
    ) {
        VoteOptionResponseDto response = voteOptionService.addOption(
                plannerId, voteId, requestDto, authentication.getName());
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
                plannerId, voteId, optionId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{plannerId}/votes/{voteId}/ballot")
    public ResponseEntity<VoteBallotResponseDto> castBallot(
            Authentication authentication,
            @PathVariable Long plannerId,
            @PathVariable Long voteId,
            @Valid @RequestBody VoteBallotRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                voteBallotService.castBallot(
                        plannerId, voteId, requestDto, authentication.getName())
        );
    }

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

    @PostMapping("/{plannerId}/votes/{voteId}/close")
    public ResponseEntity<VoteCloseResponseDto> closeVote(
            Authentication authentication,
            @PathVariable Long plannerId,
            @PathVariable Long voteId
    ) {
        return ResponseEntity.ok(
                voteConfirmService.closeVote(
                        plannerId, voteId, authentication.getName())
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
                        plannerId, voteId, dto, authentication.getName())
        );
    }

    @PostMapping("/{plannerId}/votes/remind")
    public ResponseEntity<VoteReminderResponseDto> remind(
            Authentication authentication,
            @PathVariable Long plannerId
    ) {
        return ResponseEntity.ok(
                voteReminderService.remind(plannerId, authentication.getName())
        );
    }
}
