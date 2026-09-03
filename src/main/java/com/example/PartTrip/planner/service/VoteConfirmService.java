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

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
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
        VoteResult result = calculateResult(vote);

        return VoteCloseResponseDto.builder()
                .voteId(vote.getVoteId())
                .status(vote.getStatus().name())
                .totalVoteCount(result.totalVoteCount())
                .highestVoteCount(result.highestVoteCount())
                .topOptionIds(result.selectionOptionIds())
                .tied(result.selectionRequired())
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

        VoteResult result = calculateResult(vote);
        if (result.rankedOptions().isEmpty()) {
            throw new IllegalArgumentException("확정할 투표 후보가 없습니다.");
        }

        Long requestedOptionId = dto == null ? null : dto.getOptionId();
        List<VoteOptionEntity> confirmedOptions = selectOptions(requestedOptionId, result);
        result.rankedOptions().forEach(option -> option.setConfirmed(false));
        confirmedOptions.forEach(option -> option.setConfirmed(true));

        VoteOptionEntity primaryOption = confirmedOptions.stream()
                .min(result.rankingComparator())
                .orElseThrow(() -> new IllegalArgumentException("확정할 투표 후보가 없습니다."));

        vote.setConfirmedOptionId(primaryOption.getOptionId());
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
                .confirmedOptionId(primaryOption.getOptionId())
                .tourPlaceId(primaryOption.getTourPlaceId())
                .placeName(primaryOption.getPlaceName())
                .voteCount(result.countByOptionId().getOrDefault(primaryOption.getOptionId(), 0L))
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

    private VoteResult calculateResult(VoteEntity vote) {
        Long voteId = vote.getVoteId();
        List<VoteOptionEntity> options = voteOptionRepository.findByVoteIdOrderByCreatedAtAsc(voteId);
        Map<Long, Long> countByOptionId = new HashMap<>();
        options.forEach(option -> countByOptionId.put(option.getOptionId(), 0L));

        voteRecordRepository.countByOption(voteId)
                .forEach(row -> countByOptionId.put((Long) row[0], (Long) row[1]));

        long highestVoteCount = countByOptionId.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);
        long totalVoteCount = countByOptionId.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        Comparator<VoteOptionEntity> rankingComparator = Comparator
                .comparingLong((VoteOptionEntity option) ->
                        countByOptionId.getOrDefault(option.getOptionId(), 0L))
                .reversed()
                .thenComparing(VoteOptionEntity::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(VoteOptionEntity::getOptionId);
        List<VoteOptionEntity> rankedOptions = options.stream()
                .sorted(rankingComparator)
                .toList();

        int requiredCount = Math.min(requiredOptionCount(vote), rankedOptions.size());
        if (requiredCount == 0) {
            return new VoteResult(countByOptionId, rankedOptions, List.of(), 0,
                    false, highestVoteCount, totalVoteCount, rankingComparator);
        }

        // 0표 투표는 장바구니 흐름이다. 순위를 만들지 않고 사용자가 직접
        // 고른 한 곳만 확정한다.
        if (totalVoteCount == 0) {
            return new VoteResult(countByOptionId, rankedOptions,
                    rankedOptions.stream().map(VoteOptionEntity::getOptionId).toList(),
                    1, rankedOptions.size() > 1, highestVoteCount, totalVoteCount,
                    rankingComparator);
        }

        long boundaryCount = countByOptionId.get(rankedOptions.get(requiredCount - 1).getOptionId());
        List<VoteOptionEntity> guaranteed = rankedOptions.stream()
                .filter(option -> countByOptionId.get(option.getOptionId()) > boundaryCount)
                .toList();
        List<Long> boundaryOptionIds = rankedOptions.stream()
                .filter(option -> countByOptionId.get(option.getOptionId()) == boundaryCount)
                .map(VoteOptionEntity::getOptionId)
                .toList();
        int remainingAtBoundary = requiredCount - guaranteed.size();
        boolean selectionRequired = boundaryOptionIds.size() > remainingAtBoundary;

        return new VoteResult(countByOptionId, rankedOptions, boundaryOptionIds,
                requiredCount, selectionRequired, highestVoteCount, totalVoteCount,
                rankingComparator);
    }

    private int requiredOptionCount(VoteEntity vote) {
        GroupTravelPlanEntity plan = groupTravelPlanRepository.findById(vote.getPlanId())
                .orElseThrow(() -> new IllegalArgumentException("여행 계획이 존재하지 않습니다."));
        if (plan.getStartDate() == null || plan.getEndDate() == null) {
            throw new IllegalArgumentException("여행 기간이 설정되지 않았습니다.");
        }
        long days = ChronoUnit.DAYS.between(plan.getStartDate(), plan.getEndDate()) + 1;
        if (days <= 0 || days > Integer.MAX_VALUE / 2) {
            throw new IllegalArgumentException("여행 기간이 올바르지 않습니다.");
        }
        return switch (vote.getCategory()) {
            case ACCOMMODATION -> 1;
            case RESTAURANT -> Math.toIntExact(days * 2);
            default -> Math.toIntExact(days);
        };
    }

    private List<VoteOptionEntity> selectOptions(Long requestedOptionId, VoteResult result) {
        if (result.totalVoteCount() == 0) {
            if (requestedOptionId == null) {
                if (result.rankedOptions().size() != 1) {
                    throw new IllegalArgumentException("직접 선택한 후보를 지정해주세요.");
                }
                return List.of(result.rankedOptions().get(0));
            }
            return List.of(findRequestedOption(requestedOptionId, result.rankedOptions()));
        }

        List<VoteOptionEntity> selected = new ArrayList<>(
                result.rankedOptions().subList(0, result.requiredCount()));
        if (!result.selectionRequired()) {
            if (requestedOptionId != null && selected.stream()
                    .noneMatch(option -> option.getOptionId().equals(requestedOptionId))) {
                throw new IllegalArgumentException("확정 대상인 상위 후보만 선택할 수 있습니다.");
            }
            return selected;
        }

        if (requestedOptionId == null) {
            throw new IllegalArgumentException("마지막 자리가 동점입니다. 확정할 후보를 선택해주세요.");
        }
        if (!result.selectionOptionIds().contains(requestedOptionId)) {
            throw new IllegalArgumentException("마지막 자리의 동점 후보만 선택할 수 있습니다.");
        }

        VoteOptionEntity requested = findRequestedOption(requestedOptionId, result.rankedOptions());
        List<VoteOptionEntity> resultOptions = result.rankedOptions().stream()
                .takeWhile(option -> !result.selectionOptionIds().contains(option.getOptionId()))
                .limit(result.requiredCount())
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        int boundarySeatsBeforeLast = result.requiredCount() - resultOptions.size() - 1;
        result.rankedOptions().stream()
                .filter(option -> result.selectionOptionIds().contains(option.getOptionId()))
                .filter(option -> !option.getOptionId().equals(requestedOptionId))
                .limit(boundarySeatsBeforeLast)
                .forEach(resultOptions::add);
        resultOptions.add(requested);
        return resultOptions;
    }

    private VoteOptionEntity findRequestedOption(
            Long requestedOptionId,
            List<VoteOptionEntity> options
    ) {
        return options.stream()
                .filter(option -> option.getOptionId().equals(requestedOptionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("투표 후보가 존재하지 않습니다."));
    }

    private record VoteResult(
            Map<Long, Long> countByOptionId,
            List<VoteOptionEntity> rankedOptions,
            List<Long> selectionOptionIds,
            int requiredCount,
            boolean selectionRequired,
            long highestVoteCount,
            long totalVoteCount,
            Comparator<VoteOptionEntity> rankingComparator
    ) {}
}
