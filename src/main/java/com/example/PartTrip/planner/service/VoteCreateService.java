package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.request.CreateVoteRequestDto;
import com.example.PartTrip.planner.dto.response.VoteCreateResponseDto;
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
import com.example.PartTrip.planner.repository.VoteRepository;
import com.example.PartTrip.planner.repository.VoteOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VoteCreateService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final TourPlaceRepository tourPlaceRepository;

    @Transactional
    public VoteCreateResponseDto createVote(
            Long plannerId,
            CreateVoteRequestDto dto,
            String userId
    ) {
        TravelGroupEntity group = travelGroupRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));

        GroupMemberEntity membership = groupMemberRepository
                .findByGroupIdAndUserId(plannerId, userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 플래너의 멤버만 투표를 생성할 수 있습니다."
                ));

        if (membership.getRole() != GroupRole.OWNER) {
            throw new IllegalArgumentException("플래너 그룹장만 투표를 생성할 수 있습니다.");
        }

        if (group.getStatus() != GroupStatus.PLANNING
                && group.getStatus() != GroupStatus.VOTING) {
            throw new IllegalArgumentException("현재 상태에서는 투표를 생성할 수 없습니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (dto.getDeadline() != null && !dto.getDeadline().isAfter(now)) {
            throw new IllegalArgumentException("투표 마감 시간은 현재 시간보다 이후여야 합니다.");
        }

        GroupTravelPlanEntity plan = groupTravelPlanRepository
                .findFirstByGroupIdOrderByCreatedAtDesc(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너의 여행 계획이 없습니다."));

        TourPlaceCategory category = TourPlaceCategory.from(dto.getCategory());
        if (voteRepository.findByPlanIdAndCategory(plan.getPlanId(), category).isPresent()) {
            throw new IllegalArgumentException("이미 생성된 카테고리 투표입니다.");
        }

        VoteEntity vote = new VoteEntity();
        vote.setPlanId(plan.getPlanId());
        vote.setCategory(category);
        vote.setStatus(VoteStatus.OPEN);
        vote.setDeadline(dto.getDeadline());
        vote.setCreatedAt(now);

        VoteEntity savedVote = voteRepository.save(vote);
        addInitialOptionIfPresent(savedVote, dto.getPlaceId(), userId, now);
        group.setStatus(GroupStatus.VOTING);

        return VoteCreateResponseDto.builder()
                .voteId(savedVote.getVoteId())
                .plannerId(group.getGroupId())
                .planId(plan.getPlanId())
                .category(savedVote.getCategory().name())
                .categoryLabel(savedVote.getCategory().getLabel())
                .status(savedVote.getStatus().name())
                .deadline(savedVote.getDeadline())
                .createdAt(savedVote.getCreatedAt())
                .count(0L)
                .build();
    }

    private void addInitialOptionIfPresent(
            VoteEntity vote,
            Long placeId,
            String userId,
            LocalDateTime now
    ) {
        if (placeId == null) {
            return;
        }

        TourPlaceEntity place = tourPlaceRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("관광지가 존재하지 않습니다."));
        if (place.getCategory() != null && place.getCategory() != vote.getCategory()) {
            throw new IllegalArgumentException("투표 카테고리와 관광지 카테고리가 일치하지 않습니다.");
        }

        VoteOptionEntity option = new VoteOptionEntity();
        option.setVoteId(vote.getVoteId());
        option.setTourPlaceId(place.getTourPlaceId());
        option.setPlaceName(place.getPlaceName());
        option.setAddedByUserId(userId);
        option.setCreatedAt(now);
        voteOptionRepository.save(option);
    }
}
