package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.response.ConfirmedPlaceResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerFinalResponseDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.entity.VoteOptionEntity;
import com.example.PartTrip.planner.entity.VoteRecordEntity;
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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerFinalService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteRecordRepository voteRecordRepository;
    private final TourPlaceRepository tourPlaceRepository;

    @Transactional(readOnly = true)
    public PlannerFinalResponseDto getConfirmedPlaces(
            Long plannerId,
            String userId
    ) {
        TravelGroupEntity group = travelGroupRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));

        if (!groupMemberRepository.existsByGroupIdAndUserId(plannerId, userId)) {
            throw new IllegalArgumentException("해당 플래너의 멤버만 최종 장소를 조회할 수 있습니다.");
        }

        GroupTravelPlanEntity plan = groupTravelPlanRepository
                .findFirstByGroupIdOrderByCreatedAtDesc(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너의 여행 계획이 없습니다."));

        List<VoteEntity> votes = voteRepository.findByPlanId(plan.getPlanId()).stream()
                .sorted(Comparator.comparing(vote -> vote.getCategory().ordinal()))
                .toList();

        if (votes.isEmpty()) {
            throw new IllegalArgumentException("생성된 투표가 없습니다.");
        }

        boolean allVotesConfirmed = votes.stream()
                .allMatch(vote -> vote.getStatus() == VoteStatus.CONFIRMED
                        && vote.getConfirmedOptionId() != null);
        if (!allVotesConfirmed) {
            throw new IllegalArgumentException("모든 카테고리 투표가 확정된 후 조회할 수 있습니다.");
        }

        List<Long> optionIds = votes.stream()
                .map(VoteEntity::getConfirmedOptionId)
                .toList();
        Map<Long, VoteOptionEntity> optionsById = voteOptionRepository
                .findAllById(optionIds)
                .stream()
                .collect(Collectors.toMap(VoteOptionEntity::getOptionId, Function.identity()));

        if (optionsById.size() != optionIds.size()) {
            throw new IllegalArgumentException("확정된 투표 후보 정보를 찾을 수 없습니다.");
        }

        Set<Long> tourPlaceIds = optionsById.values().stream()
                .map(VoteOptionEntity::getTourPlaceId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        Map<Long, TourPlaceEntity> placesById = tourPlaceIds.isEmpty()
                ? Map.of()
                : tourPlaceRepository.findAllById(tourPlaceIds).stream()
                        .collect(Collectors.toMap(
                                TourPlaceEntity::getTourPlaceId,
                                Function.identity()
                        ));

        Map<Long, Long> confirmedVoteCountByVoteId = voteRecordRepository
                .findByVoteIdIn(votes.stream().map(VoteEntity::getVoteId).toList())
                .stream()
                .filter(record -> isConfirmedRecord(record, votes))
                .collect(Collectors.groupingBy(
                        VoteRecordEntity::getVoteId,
                        Collectors.counting()
                ));

        List<ConfirmedPlaceResponseDto> confirmedPlaces = votes.stream()
                .map(vote -> toConfirmedPlace(
                        vote,
                        optionsById.get(vote.getConfirmedOptionId()),
                        placesById,
                        confirmedVoteCountByVoteId.getOrDefault(vote.getVoteId(), 0L)
                ))
                .toList();

        return PlannerFinalResponseDto.builder()
                .plannerId(group.getGroupId())
                .title(group.getGroupName())
                .countryName(plan.getCountryName())
                .cityName(plan.getCityName())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .status(group.getStatus().name())
                .places(confirmedPlaces)
                .build();
    }

    private boolean isConfirmedRecord(
            VoteRecordEntity record,
            List<VoteEntity> votes
    ) {
        return votes.stream()
                .filter(vote -> vote.getVoteId().equals(record.getVoteId()))
                .map(VoteEntity::getConfirmedOptionId)
                .anyMatch(optionId -> optionId.equals(record.getOptionId()));
    }

    private ConfirmedPlaceResponseDto toConfirmedPlace(
            VoteEntity vote,
            VoteOptionEntity option,
            Map<Long, TourPlaceEntity> placesById,
            long voteCount
    ) {
        TourPlaceEntity place = option.getTourPlaceId() == null
                ? null
                : placesById.get(option.getTourPlaceId());

        return ConfirmedPlaceResponseDto.builder()
                .voteId(vote.getVoteId())
                .category(vote.getCategory().name())
                .categoryLabel(vote.getCategory().getLabel())
                .optionId(option.getOptionId())
                .tourPlaceId(option.getTourPlaceId())
                .placeName(option.getPlaceName())
                .imageUrl(place == null ? null : place.getImageUrl())
                .address(place == null ? null : place.getAddress())
                .rating(place == null ? null : place.getRating())
                .voteCount(voteCount)
                .build();
    }
}
