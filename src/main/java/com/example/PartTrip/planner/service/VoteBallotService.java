package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.notification.event.VoteParticipatedEvent;
import com.example.PartTrip.planner.dto.request.VoteBallotRequestDto;
import com.example.PartTrip.planner.dto.response.VoteBallotResponseDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.PlannerCityEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.entity.VoteOptionEntity;
import com.example.PartTrip.planner.entity.VoteRecordEntity;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.enums.VoteStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.PlannerCityRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteOptionRepository;
import com.example.PartTrip.planner.repository.VoteRecordRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteBallotService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final PlannerCityRepository plannerCityRepository;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteRecordRepository voteRecordRepository;
    private final TourPlaceRepository tourPlaceRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public VoteBallotResponseDto castBallot(
            Long plannerId,
            Long voteId,
            VoteBallotRequestDto dto,
            String userId
    ) {
        requireMember(plannerId, userId);

        VoteEntity vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표가 존재하지 않습니다."));

        groupTravelPlanRepository.findByPlanIdAndGroupId(vote.getPlanId(), plannerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플래너의 투표가 아닙니다."));

        validateVoteIsOpen(vote);

        VoteOptionEntity option = voteOptionRepository
                .findByOptionIdAndVoteId(dto.getOptionId(), voteId)
                .orElseThrow(() -> new IllegalArgumentException("해당 투표의 후보가 아닙니다."));

        return record(vote, option, userId);
    }

    @Transactional
    public VoteBallotResponseDto voteForPlace(Long plannerId, Long tourPlaceId, String userId) {
        TravelGroupEntity group = travelGroupRepository.findByIdForUpdate(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));
        requireMember(plannerId, userId);

        if (group.getStatus() != GroupStatus.PLANNING && group.getStatus() != GroupStatus.VOTING) {
            throw new IllegalArgumentException("투표가 끝난 플래너입니다.");
        }

        GroupTravelPlanEntity plan = requirePlan(plannerId);
        TourPlaceEntity place = requirePlaceOfPlan(plan, tourPlaceId);

        LocalDateTime now = LocalDateTime.now();
        VoteEntity vote = voteRepository.findByPlanIdAndCategory(plan.getPlanId(), place.getCategory())
                .orElseGet(() -> voteRepository.save(newOpenVote(plan.getPlanId(), place, now)));
        validateVoteIsOpen(vote);

        VoteOptionEntity option = voteOptionRepository
                .findByVoteIdAndTourPlaceId(vote.getVoteId(), place.getTourPlaceId())
                .orElseGet(() -> voteOptionRepository.save(newOption(vote, place, userId, now)));

        if (group.getStatus() == GroupStatus.PLANNING) {
            group.setStatus(GroupStatus.VOTING);
        }
        return record(vote, option, userId);
    }

    @Transactional
    public void cancelPlaceVote(Long plannerId, Long tourPlaceId, String userId) {
        requireMember(plannerId, userId);
        GroupTravelPlanEntity plan = requirePlan(plannerId);
        TourPlaceEntity place = tourPlaceRepository.findById(tourPlaceId)
                .orElseThrow(() -> new IllegalArgumentException("관광지가 존재하지 않습니다."));
        if (place.getCategory() == null) {
            return;
        }

        VoteEntity vote = voteRepository.findByPlanIdAndCategory(plan.getPlanId(), place.getCategory())
                .orElse(null);
        if (vote == null) {
            return;
        }
        validateVoteIsOpen(vote);

        voteOptionRepository.findByVoteIdAndTourPlaceId(vote.getVoteId(), tourPlaceId)
                .flatMap(option -> voteRecordRepository.findByOptionIdAndUserId(option.getOptionId(), userId))
                .ifPresent(voteRecordRepository::delete);
    }

    private VoteBallotResponseDto record(VoteEntity vote, VoteOptionEntity option, String userId) {
        VoteRecordEntity existing = voteRecordRepository
                .findByOptionIdAndUserId(option.getOptionId(), userId)
                .orElse(null);
        if (existing != null) {
            return toResponse(existing, option);
        }

        boolean firstInVote = !voteRecordRepository.existsByVoteIdAndUserId(vote.getVoteId(), userId);

        VoteRecordEntity record = new VoteRecordEntity();
        record.setVoteId(vote.getVoteId());
        record.setOptionId(option.getOptionId());
        record.setUserId(userId);
        record.setVotedAt(LocalDateTime.now());
        VoteRecordEntity saved = voteRecordRepository.save(record);

        if (firstInVote) {
            eventPublisher.publishEvent(new VoteParticipatedEvent(vote.getVoteId(), userId));
        }
        return toResponse(saved, option);
    }

    private TourPlaceEntity requirePlaceOfPlan(GroupTravelPlanEntity plan, Long tourPlaceId) {
        TourPlaceEntity place = tourPlaceRepository.findById(tourPlaceId)
                .orElseThrow(() -> new IllegalArgumentException("관광지가 존재하지 않습니다."));
        if (place.getCategory() == null) {
            throw new IllegalArgumentException("카테고리가 없는 장소에는 투표할 수 없습니다.");
        }

        Set<String> cities = plannerCityRepository.findByPlanIdOrderBySeqAsc(plan.getPlanId()).stream()
                .map(PlannerCityEntity::getCityName)
                .collect(Collectors.toSet());
        if (cities.isEmpty()) {
            cities = Set.of(plan.getCityName());
        }
        if (!cities.contains(place.getCityName())) {
            throw new IllegalArgumentException("이 여행의 도시에 있는 장소가 아닙니다.");
        }
        return place;
    }

    private void requireMember(Long plannerId, String userId) {
        if (!groupMemberRepository.existsByGroupIdAndUserId(plannerId, userId)) {
            throw new IllegalArgumentException("해당 플래너의 멤버만 투표할 수 있습니다.");
        }
    }

    private GroupTravelPlanEntity requirePlan(Long plannerId) {
        return groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너의 여행 계획이 없습니다."));
    }

    private void validateVoteIsOpen(VoteEntity vote) {
        if (vote.getStatus() != VoteStatus.OPEN) {
            throw new IllegalArgumentException("진행 중인 투표에만 참여할 수 있습니다.");
        }

        if (vote.getDeadline() != null && !vote.getDeadline().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("마감된 투표입니다.");
        }
    }

    private static VoteEntity newOpenVote(Long planId, TourPlaceEntity place, LocalDateTime now) {
        VoteEntity vote = new VoteEntity();
        vote.setPlanId(planId);
        vote.setCategory(place.getCategory());
        vote.setStatus(VoteStatus.OPEN);
        vote.setCreatedAt(now);
        return vote;
    }

    private static VoteOptionEntity newOption(
            VoteEntity vote, TourPlaceEntity place, String userId, LocalDateTime now) {
        VoteOptionEntity option = new VoteOptionEntity();
        option.setVoteId(vote.getVoteId());
        option.setTourPlaceId(place.getTourPlaceId());
        option.setPlaceName(place.getPlaceName());
        option.setAddedByUserId(userId);
        option.setCreatedAt(now);
        return option;
    }

    private static VoteBallotResponseDto toResponse(VoteRecordEntity record, VoteOptionEntity option) {
        return VoteBallotResponseDto.builder()
                .voteRecordId(record.getVoteRecordId())
                .voteId(record.getVoteId())
                .optionId(record.getOptionId())
                .placeName(option.getPlaceName())
                .changed(false)
                .votedAt(record.getVotedAt())
                .build();
    }
}
