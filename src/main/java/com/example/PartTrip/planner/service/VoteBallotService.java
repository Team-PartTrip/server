package com.example.PartTrip.planner.service;

import com.example.PartTrip.notification.event.VoteParticipatedEvent;
import com.example.PartTrip.planner.dto.request.VoteBallotRequestDto;
import com.example.PartTrip.planner.dto.response.VoteBallotResponseDto;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.entity.VoteOptionEntity;
import com.example.PartTrip.planner.entity.VoteRecordEntity;
import com.example.PartTrip.planner.enums.VoteStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.VoteOptionRepository;
import com.example.PartTrip.planner.repository.VoteRecordRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VoteBallotService {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteRecordRepository voteRecordRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public VoteBallotResponseDto castBallot(
            Long plannerId,
            Long voteId,
            VoteBallotRequestDto dto,
            String userId
    ) {
        if (!groupMemberRepository.existsByGroupIdAndUserId(plannerId, userId)) {
            throw new IllegalArgumentException("해당 플래너의 멤버만 투표할 수 있습니다.");
        }

        VoteEntity vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표가 존재하지 않습니다."));

        groupTravelPlanRepository.findByPlanIdAndGroupId(vote.getPlanId(), plannerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플래너의 투표가 아닙니다."));

        validateVoteIsOpen(vote);

        VoteOptionEntity option = voteOptionRepository
                .findByOptionIdAndVoteId(dto.getOptionId(), voteId)
                .orElseThrow(() -> new IllegalArgumentException("해당 투표의 후보가 아닙니다."));

        VoteRecordEntity record = voteRecordRepository
                .findByVoteIdAndUserId(voteId, userId)
                .orElse(null);

        boolean isNew = record == null;
        boolean changed = !isNew && !record.getOptionId().equals(option.getOptionId());

        if (isNew) {
            record = new VoteRecordEntity();
            record.setVoteId(voteId);
            record.setUserId(userId);
        }

        record.setOptionId(option.getOptionId());
        record.setVotedAt(LocalDateTime.now());
        VoteRecordEntity savedRecord = voteRecordRepository.save(record);

        if (isNew) {
            eventPublisher.publishEvent(new VoteParticipatedEvent(voteId, userId));
        }

        return VoteBallotResponseDto.builder()
                .voteRecordId(savedRecord.getVoteRecordId())
                .voteId(savedRecord.getVoteId())
                .optionId(savedRecord.getOptionId())
                .placeName(option.getPlaceName())
                .changed(changed)
                .votedAt(savedRecord.getVotedAt())
                .build();
    }

    private void validateVoteIsOpen(VoteEntity vote) {
        if (vote.getStatus() != VoteStatus.OPEN) {
            throw new IllegalArgumentException("진행 중인 투표에만 참여할 수 있습니다.");
        }

        if (vote.getDeadline() != null && !vote.getDeadline().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("마감된 투표입니다.");
        }
    }
}
