package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.response.PlannerDetailResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlannerDetailService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final PlannerInviteLinkFactory inviteLinkFactory;

    @Transactional(readOnly = true)
    public PlannerDetailResponseDto getPlannerDetail(
            Long plannerId,
            String userId
    ) {
        GroupMemberEntity membership = groupMemberRepository
                .findByGroupIdAndUserId(plannerId, userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 플래너를 조회할 권한이 없습니다."
                ));

        TravelGroupEntity group = travelGroupRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));

        GroupTravelPlanEntity plan = groupTravelPlanRepository
                .findFirstByGroupIdOrderByCreatedAtDesc(plannerId)
                .orElse(null);

        long joinedMemberCount = groupMemberRepository.countByGroupId(plannerId);

        return PlannerDetailResponseDto.builder()
                .plannerId(group.getGroupId())
                .title(group.getGroupName())
                .countryName(plan == null ? null : plan.getCountryName())
                .cityName(plan == null ? null : plan.getCityName())
                .startDate(plan == null ? null : plan.getStartDate())
                .endDate(plan == null ? null : plan.getEndDate())
                .status(group.getStatus().name())
                .role(membership.getRole().name())
                .memberCount(group.getHeadcount())
                .joinedMemberCount(joinedMemberCount)
                .inviteLink(inviteLinkFactory.create(group.getInviteCode()))
                .build();
    }
}
