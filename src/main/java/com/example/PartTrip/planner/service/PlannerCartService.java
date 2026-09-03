package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.request.PlannerCartRequestDto;
import com.example.PartTrip.planner.dto.response.RandomPlaceResponseDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.entity.VoteOptionEntity;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.enums.VoteStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteOptionRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerCartService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final TourPlaceRepository tourPlaceRepository;

    @Transactional
    public String addPlaces(
            Long plannerId,
            PlannerCartRequestDto dto,
            String userId
    ) {
        TravelGroupEntity group = requireMemberForUpdate(plannerId, userId);
        GroupTravelPlanEntity plan = requirePlan(plannerId);
        List<Long> placeIds = dto.getPlaceIds().stream().distinct().toList();
        Map<Long, TourPlaceEntity> placesById = tourPlaceRepository.findAllById(placeIds).stream()
                .collect(Collectors.toMap(TourPlaceEntity::getTourPlaceId, Function.identity()));
        placeIds.stream()
                .filter(placeId -> !placesById.containsKey(placeId))
                .findFirst()
                .ifPresent(placeId -> {
                    throw new IllegalArgumentException("관광지가 존재하지 않습니다: " + placeId);
                });

        Set<TourPlaceCategory> requiredCategories = placesById.values().stream()
                .map(TourPlaceEntity::getCategory)
                .collect(Collectors.toSet());
        if (requiredCategories.contains(null)) {
            throw new IllegalArgumentException("카테고리가 없는 관광지는 담을 수 없습니다.");
        }

        Map<TourPlaceCategory, VoteEntity> votesByCategory = new EnumMap<>(TourPlaceCategory.class);
        voteRepository.findByPlanId(plan.getPlanId())
                .forEach(vote -> votesByCategory.put(vote.getCategory(), vote));
        List<VoteEntity> newVotes = requiredCategories.stream()
                .filter(category -> !votesByCategory.containsKey(category))
                .map(category -> newOpenVote(plan.getPlanId(), category))
                .toList();
        voteRepository.saveAll(newVotes)
                .forEach(vote -> votesByCategory.put(vote.getCategory(), vote));

        List<VoteEntity> targetVotes = requiredCategories.stream().map(votesByCategory::get).toList();
        if (targetVotes.stream().anyMatch(vote -> vote.getStatus() != VoteStatus.OPEN)) {
            throw new IllegalArgumentException("마감된 카테고리 투표에는 장소를 담을 수 없습니다.");
        }

        Set<String> existingPairs = voteOptionRepository
                .findByVoteIdInOrderByCreatedAtAsc(targetVotes.stream().map(VoteEntity::getVoteId).toList())
                .stream()
                .filter(option -> option.getTourPlaceId() != null)
                .map(option -> option.getVoteId() + ":" + option.getTourPlaceId())
                .collect(Collectors.toCollection(HashSet::new));
        LocalDateTime now = LocalDateTime.now();
        List<VoteOptionEntity> optionsToSave = placeIds.stream()
                .map(placesById::get)
                .filter(place -> !existingPairs.contains(
                        votesByCategory.get(place.getCategory()).getVoteId() + ":" + place.getTourPlaceId()))
                .map(place -> newOption(
                        votesByCategory.get(place.getCategory()).getVoteId(), place, userId, now))
                .toList();
        voteOptionRepository.saveAll(optionsToSave);
        int addedCount = optionsToSave.size();

        if (addedCount > 0) {
            group.setStatus(GroupStatus.VOTING);
        }
        return addedCount + "개 장소를 장바구니에 담았습니다.";
    }

    private VoteEntity newOpenVote(Long planId, TourPlaceCategory category) {
        VoteEntity vote = new VoteEntity();
        vote.setPlanId(planId);
        vote.setCategory(category);
        vote.setStatus(VoteStatus.OPEN);
        vote.setCreatedAt(LocalDateTime.now());
        return vote;
    }

    private VoteOptionEntity newOption(
            Long voteId,
            TourPlaceEntity place,
            String userId,
            LocalDateTime createdAt
    ) {
        VoteOptionEntity option = new VoteOptionEntity();
        option.setVoteId(voteId);
        option.setTourPlaceId(place.getTourPlaceId());
        option.setPlaceName(place.getPlaceName());
        option.setAddedByUserId(userId);
        option.setCreatedAt(createdAt);
        return option;
    }

    @Transactional(readOnly = true)
    public RandomPlaceResponseDto selectRandom(Long plannerId, String userId) {
        requireMember(plannerId, userId);
        GroupTravelPlanEntity plan = requirePlan(plannerId);
        List<Long> voteIds = voteRepository.findByPlanId(plan.getPlanId()).stream()
                .map(VoteEntity::getVoteId)
                .toList();
        if (voteIds.isEmpty()) {
            throw new IllegalArgumentException("장바구니에 담긴 장소가 없습니다.");
        }

        List<VoteOptionEntity> options = voteOptionRepository
                .findByVoteIdInOrderByCreatedAtAsc(voteIds);
        if (options.isEmpty()) {
            throw new IllegalArgumentException("장바구니에 담긴 장소가 없습니다.");
        }

        VoteOptionEntity selected = options.get(
                ThreadLocalRandom.current().nextInt(options.size())
        );
        return RandomPlaceResponseDto.builder()
                .placeId(selected.getTourPlaceId())
                .placeName(selected.getPlaceName())
                .build();
    }

    private TravelGroupEntity requireMember(Long plannerId, String userId) {
        TravelGroupEntity group = travelGroupRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));
        if (!groupMemberRepository.existsByGroupIdAndUserId(plannerId, userId)) {
            throw new IllegalArgumentException("해당 플래너의 멤버만 장바구니를 사용할 수 있습니다.");
        }
        return group;
    }

    private TravelGroupEntity requireMemberForUpdate(Long plannerId, String userId) {
        TravelGroupEntity group = travelGroupRepository.findByIdForUpdate(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));
        if (!groupMemberRepository.existsByGroupIdAndUserId(plannerId, userId)) {
            throw new IllegalArgumentException("해당 플래너의 멤버만 장바구니를 사용할 수 있습니다.");
        }
        return group;
    }

    private GroupTravelPlanEntity requirePlan(Long plannerId) {
        return groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너의 여행 계획이 없습니다."));
    }

}
