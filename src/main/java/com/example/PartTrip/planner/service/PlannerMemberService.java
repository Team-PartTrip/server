package com.example.PartTrip.planner.service;

import com.example.PartTrip.notification.event.GroupInviteAcceptedEvent;
import com.example.PartTrip.planner.dto.request.JoinPlannerRequestDto;
import com.example.PartTrip.planner.dto.request.InvitePlannerMembersRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerInviteResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerJoinResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerMemberService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;

    @Transactional
    public PlannerJoinResponseDto joinPlanner(
            JoinPlannerRequestDto dto,
            String userId
    ) {
        String inviteCode = dto.getInviteCode().trim().toUpperCase();

        TravelGroupEntity group = travelGroupRepository
                .findByInviteCodeForUpdate(inviteCode)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 초대 코드입니다."));

        if (groupMemberRepository.existsByGroupIdAndUserId(group.getGroupId(), userId)) {
            throw new IllegalArgumentException("이미 참여한 플래너입니다.");
        }

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

    @Transactional
    public PlannerInviteResponseDto inviteMembers(
            Long plannerId,
            InvitePlannerMembersRequestDto dto,
            String userId
    ) {
        TravelGroupEntity group = travelGroupRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));

        GroupMemberEntity requester = groupMemberRepository
                .findByGroupIdAndUserId(plannerId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플래너의 멤버가 아닙니다."));
        if (requester.getRole() != GroupRole.OWNER) {
            throw new IllegalArgumentException("플래너 그룹장만 멤버를 초대할 수 있습니다.");
        }

        List<String> newUserIds = dto.getUserIds().stream()
                .filter(id -> id != null && !id.isBlank())
                .map(String::trim)
                .distinct()
                .filter(id -> !groupMemberRepository.existsByGroupIdAndUserId(plannerId, id))
                .toList();

        long currentCount = groupMemberRepository.countByGroupId(plannerId);
        if (currentCount + newUserIds.size() > group.getHeadcount()) {
            throw new IllegalArgumentException("설정한 여행 인원을 초과하여 초대할 수 없습니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        for (String invitedUserId : newUserIds) {
            if (!userRepository.existsByUserId(invitedUserId)) {
                throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + invitedUserId);
            }

            GroupMemberEntity member = new GroupMemberEntity();
            member.setGroupId(plannerId);
            member.setUserId(invitedUserId);
            member.setRole(GroupRole.MEMBER);
            member.setJoinedAt(now);
            groupMemberRepository.save(member);
        }

        return PlannerInviteResponseDto.builder()
                .inviteLink("/planner/join?inviteCode=" + group.getInviteCode())
                .build();
    }
}
