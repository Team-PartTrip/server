package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.planner.dto.request.CreateVoteRequestDto;
import com.example.PartTrip.planner.dto.response.VoteCreateResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.enums.VoteStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
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
                .build();
    }
}
