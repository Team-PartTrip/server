package com.example.PartTrip.planner.service;

import com.example.PartTrip.notification.event.GroupInviteAcceptedEvent;
import com.example.PartTrip.planner.dto.request.JoinPlannerRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerJoinResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlannerMemberService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PlannerScheduleLockService plannerScheduleLockService;

    @Transactional
    public PlannerJoinResponseDto joinPlanner(
            JoinPlannerRequestDto dto,
            String userId
    ) {
        String inviteCode = dto.getInviteCode().trim().toUpperCase();

        TravelGroupEntity group = travelGroupRepository
                .findByInviteCodeForUpdate(inviteCode)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 초대 코드입니다."));

        if (group.getStatus() != GroupStatus.PLANNING) {
            throw new IllegalArgumentException("계획 중인 플래너에만 참여할 수 있습니다.");
        }

        if (groupMemberRepository.existsByGroupIdAndUserId(group.getGroupId(), userId)) {
            throw new IllegalArgumentException("이미 참여한 플래너입니다.");
        }

        plannerScheduleLockService.lockUser(userId);
        validateNoOverlappingPlan(group.getGroupId(), userId);

        long currentMemberCount = groupMemberRepository.countByGroupId(group.getGroupId());
        if (currentMemberCount >= group.getHeadcount()) {
            throw new IllegalArgumentException("참여 가능한 인원이 모두 찼습니다.");
        }

        GroupMemberEntity member = new GroupMemberEntity();
        member.setGroupId(group.getGroupId());
        member.setUserId(userId);
        member.setRole(GroupRole.MEMBER);
        member.setJoinedAt(LocalDateTime.now());
        groupMemberRepository.save(member);

        eventPublisher.publishEvent(new GroupInviteAcceptedEvent(group.getGroupId(), userId));

        return PlannerJoinResponseDto.builder()
                .plannerId(group.getGroupId())
                .title(group.getGroupName())
                .role(member.getRole().name())
                .status(group.getStatus().name())
                .memberCount(group.getHeadcount())
                .joinedMemberCount(currentMemberCount + 1)
                .build();
    }

    private void validateNoOverlappingPlan(Long groupId, String userId) {
        groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(groupId)
                .filter(plan -> groupTravelPlanRepository.existsOverlappingPlanForUser(
                        userId, plan.getStartDate(), plan.getEndDate()))
                .ifPresent(plan -> {
                    throw new IllegalArgumentException("해당 기간에 이미 등록된 여행 계획이 있습니다.");
                });
    }

}
