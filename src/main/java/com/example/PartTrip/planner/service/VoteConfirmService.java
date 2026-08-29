package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.request.VoteConfirmRequestDto;
import com.example.PartTrip.planner.dto.response.VoteCloseResponseDto;
import com.example.PartTrip.planner.dto.response.VoteConfirmResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.entity.VoteOptionEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.enums.VoteStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteOptionRepository;
import com.example.PartTrip.planner.repository.VoteRecordRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VoteConfirmService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteRecordRepository voteRecordRepository;

    @Transactional
    public VoteCloseResponseDto closeVote(
            Long plannerId,
            Long voteId,
            String userId
    ) {
        requireOwner(plannerId, userId);
        VoteEntity vote = requireVoteOfPlannerForUpdate(plannerId, voteId);

        if (vote.getStatus() == VoteStatus.CONFIRMED) {
            throw new IllegalArgumentException("이미 최종 후보가 확정된 투표입니다.");
        }

        vote.setStatus(VoteStatus.CLOSED);
        VoteResult result = calculateResult(voteId);

        return VoteCloseResponseDto.builder()
                .voteId(vote.getVoteId())
                .status(vote.getStatus().name())
                .totalVoteCount(result.totalVoteCount())
                .highestVoteCount(result.highestVoteCount())
                .topOptionIds(result.topOptionIds())
                .tied(result.topOptionIds().size() > 1)
                .build();
    }

    @Transactional
    public VoteConfirmResponseDto confirmVote(
            Long plannerId,
            Long voteId,
            VoteConfirmRequestDto dto,
            String userId
    ) {
        TravelGroupEntity group = requireOwner(plannerId, userId);
        VoteEntity vote = requireVoteOfPlannerForUpdate(plannerId, voteId);

        if (vote.getStatus() != VoteStatus.CLOSED) {
            throw new IllegalArgumentException("마감된 투표만 최종 확정할 수 있습니다.");
        }

        VoteResult result = calculateResult(voteId);
        if (result.topOptionIds().isEmpty()) {
            throw new IllegalArgumentException("확정할 투표 후보가 없습니다.");
        }

        Long selectedOptionId = selectOptionId(dto.getOptionId(), result.topOptionIds());
        VoteOptionEntity selectedOption = voteOptionRepository
                .findByOptionIdAndVoteId(selectedOptionId, voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표 후보가 존재하지 않습니다."));

        vote.setConfirmedOptionId(selectedOptionId);
        vote.setStatus(VoteStatus.CONFIRMED);

        GroupTravelPlanEntity plan = groupTravelPlanRepository.findById(vote.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("여행 계획이 존재하지 않습니다."));

        boolean allVotesConfirmed = voteRepository.findByPlanId(plan.getPlanId()).stream()
                .allMatch(planVote -> planVote.getStatus() == VoteStatus.CONFIRMED);
        if (allVotesConfirmed) {
            group.setStatus(GroupStatus.CONFIRMED);
        }

        return VoteConfirmResponseDto.builder()
                .voteId(vote.getVoteId())
                .voteStatus(vote.getStatus().name())
                .confirmedOptionId(selectedOption.getOptionId())
                .tourPlaceId(selectedOption.getTourPlaceId())
                .placeName(selectedOption.getPlaceName())
                .voteCount(result.countByOptionId().getOrDefault(selectedOptionId, 0L))
                .plannerStatus(group.getStatus().name())
                .build();
    }

    private TravelGroupEntity requireOwner(Long plannerId, String userId) {
        TravelGroupEntity group = travelGroupRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));

        GroupMemberEntity membership = groupMemberRepository
                .findByGroupIdAndUserId(plannerId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플래너의 멤버가 아닙니다."));

        if (membership.getRole() != GroupRole.OWNER) {
            throw new IllegalArgumentException("플래너 그룹장만 투표를 마감하고 확정할 수 있습니다.");
        }
        return group;
    }

    private VoteEntity requireVoteOfPlannerForUpdate(Long plannerId, Long voteId) {
        VoteEntity vote = voteRepository.findByVoteIdForUpdate(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표가 존재하지 않습니다."));

        groupTravelPlanRepository.findByPlanIdAndGroupId(vote.getPlanId(), plannerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플래너의 투표가 아닙니다."));
        return vote;
    }

    private VoteResult calculateResult(Long voteId) {
        List<VoteOptionEntity> options = voteOptionRepository.findByVoteIdOrderByCreatedAtAsc(voteId);
        Map<Long, Long> countByOptionId = new HashMap<>();
        options.forEach(option -> countByOptionId.put(option.getOptionId(), 0L));

        voteRecordRepository.countByOption(voteId)
                .forEach(row -> countByOptionId.put((Long) row[0], (Long) row[1]));

        long highestVoteCount = countByOptionId.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);
        List<Long> topOptionIds = countByOptionId.entrySet().stream()
                .filter(entry -> entry.getValue() == highestVoteCount)
                .map(Map.Entry::getKey)
                .toList();
        long totalVoteCount = countByOptionId.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        return new VoteResult(countByOptionId, topOptionIds, highestVoteCount, totalVoteCount);
    }

    private Long selectOptionId(Long requestedOptionId, List<Long> topOptionIds) {
        if (requestedOptionId == null) {
            if (topOptionIds.size() != 1) {
                throw new IllegalArgumentException("동점 투표입니다. 확정할 공동 1위 후보를 선택해주세요.");
            }
            return topOptionIds.get(0);
        }

        if (!topOptionIds.contains(requestedOptionId)) {
            throw new IllegalArgumentException("최다 득표 후보만 최종 확정할 수 있습니다.");
        }
        return requestedOptionId;
    }

    private record VoteResult(
            Map<Long, Long> countByOptionId,
            List<Long> topOptionIds,
            long highestVoteCount,
            long totalVoteCount
    ) {}
}
