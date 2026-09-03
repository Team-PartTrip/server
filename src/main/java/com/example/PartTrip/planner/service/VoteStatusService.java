package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.response.VoteOptionStatusResponseDto;
import com.example.PartTrip.planner.dto.response.VoteStatusResponseDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.entity.VoteOptionEntity;
import com.example.PartTrip.planner.entity.VoteRecordEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteOptionRepository;
import com.example.PartTrip.planner.repository.VoteRecordRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteStatusService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteRecordRepository voteRecordRepository;
    private final TourPlaceRepository tourPlaceRepository;

    @Transactional(readOnly = true)
    public List<VoteStatusResponseDto> getVotes(Long plannerId, String userId) {
        requirePlannerMember(plannerId, userId);

        List<VoteEntity> votes = voteRepository.findLatestPlanVotes(plannerId);

        if (votes.isEmpty()) {
            // 투표가 없는 것과 계획 자체가 없는 것은 다르다.
            // 비어 있을 때만 확인하므로 정상 경로에서는 왕복이 늘지 않는다.
            groupTravelPlanRepository
                    .findFirstByGroupIdOrderByCreatedAtDesc(plannerId)
                    .orElseThrow(() -> new IllegalArgumentException("플래너의 여행 계획이 없습니다."));
            return List.of();
        }

        return buildResponses(plannerId, votes, userId);
    }

    @Transactional(readOnly = true)
    public VoteStatusResponseDto getVote(
            Long plannerId,
            Long voteId,
            String userId
    ) {
        requirePlannerMember(plannerId, userId);

        VoteEntity vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표가 존재하지 않습니다."));

        groupTravelPlanRepository.findByPlanIdAndGroupId(vote.getPlanId(), plannerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플래너의 투표가 아닙니다."));

        return buildResponses(plannerId, List.of(vote), userId).get(0);
    }

    private List<VoteStatusResponseDto> buildResponses(
            Long plannerId,
            List<VoteEntity> votes,
            String userId
    ) {
        List<Long> voteIds = votes.stream().map(VoteEntity::getVoteId).toList();

        // 후보 + 관광지를 한 번에 받는다
        List<VoteOptionEntity> options = new ArrayList<>();
        Map<Long, TourPlaceEntity> placesById = new HashMap<>();
        for (Object[] row : voteOptionRepository.findOptionsWithPlaces(voteIds)) {
            VoteOptionEntity option = (VoteOptionEntity) row[0];
            TourPlaceEntity place = (TourPlaceEntity) row[1];
            options.add(option);
            if (place != null) {
                placesById.put(place.getTourPlaceId(), place);
            }
        }

        List<VoteRecordEntity> records = voteRecordRepository.findByVoteIdIn(voteIds);

        Map<Long, List<VoteOptionEntity>> optionsByVoteId = options.stream()
                .collect(Collectors.groupingBy(VoteOptionEntity::getVoteId));
        Map<Long, List<VoteRecordEntity>> recordsByVoteId = records.stream()
                .collect(Collectors.groupingBy(VoteRecordEntity::getVoteId));

        long eligibleMemberCount = groupMemberRepository.countByGroupId(plannerId);

        return votes.stream()
                .map(vote -> toVoteResponse(
                        plannerId,
                        vote,
                        optionsByVoteId.getOrDefault(vote.getVoteId(), List.of()),
                        recordsByVoteId.getOrDefault(vote.getVoteId(), List.of()),
                        placesById,
                        userId,
                        eligibleMemberCount
                ))
                .toList();
    }

    private VoteStatusResponseDto toVoteResponse(
            Long plannerId,
            VoteEntity vote,
            List<VoteOptionEntity> options,
            List<VoteRecordEntity> records,
            Map<Long, TourPlaceEntity> placesById,
            String userId,
            long eligibleMemberCount
    ) {
        Map<Long, Long> countByOptionId = new HashMap<>();
        for (VoteRecordEntity record : records) {
            countByOptionId.merge(record.getOptionId(), 1L, Long::sum);
        }

        Long selectedOptionId = records.stream()
                .filter(record -> userId.equals(record.getUserId()))
                .map(VoteRecordEntity::getOptionId)
                .findFirst()
                .orElse(null);

        List<VoteOptionStatusResponseDto> optionResponses = options.stream()
                .map(option -> toOptionResponse(
                        option,
                        placesById.get(option.getTourPlaceId()),
                        countByOptionId.getOrDefault(option.getOptionId(), 0L),
                        selectedOptionId,
                        vote.getConfirmedOptionId()
                ))
                .toList();

        boolean deadlinePassed = vote.getDeadline() != null
                && !vote.getDeadline().isAfter(LocalDateTime.now());

        return VoteStatusResponseDto.builder()
                .voteId(vote.getVoteId())
                .plannerId(plannerId)
                .category(vote.getCategory().name())
                .categoryLabel(vote.getCategory().getLabel())
                .status(vote.getStatus().name())
                .deadline(vote.getDeadline())
                .deadlinePassed(deadlinePassed)
                .eligibleMemberCount(eligibleMemberCount)
                .votedMemberCount((long) records.size())
                .confirmedOptionId(vote.getConfirmedOptionId())
                .options(optionResponses)
                .build();
    }

    private VoteOptionStatusResponseDto toOptionResponse(
            VoteOptionEntity option,
            TourPlaceEntity place,
            long voteCount,
            Long selectedOptionId,
            Long confirmedOptionId
    ) {
        return VoteOptionStatusResponseDto.builder()
                .optionId(option.getOptionId())
                .tourPlaceId(option.getTourPlaceId())
                .placeName(option.getPlaceName())
                .imageUrl(place == null ? null : place.getImageUrl())
                .address(place == null ? null : place.getAddress())
                .rating(place == null ? null : place.getRating())
                .addedByUserId(option.getAddedByUserId())
                .voteCount(voteCount)
                .selectedByMe(option.getOptionId().equals(selectedOptionId))
                .confirmed(option.getOptionId().equals(confirmedOptionId))
                .build();
    }

    /**
     * 멤버인지 확인한다.
     *
     * 예전에는 "플래너가 있나"와 "내가 멤버인가"를 따로 물어 왕복이 두 번이었다.
     * 멤버면 플래너도 있는 것이므로, 통과하는 경우에는 한 번이면 된다.
     * 막히는 경우에만 이유를 가르려고 한 번 더 묻는다.
     */
    private void requirePlannerMember(Long plannerId, String userId) {
        if (groupMemberRepository.existsByGroupIdAndUserId(plannerId, userId)) {
            return;
        }
        if (!travelGroupRepository.existsById(plannerId)) {
            throw new IllegalArgumentException("플래너가 존재하지 않습니다.");
        }
        throw new IllegalArgumentException("해당 플래너의 멤버만 투표를 조회할 수 있습니다.");
    }
}
