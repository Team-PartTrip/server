package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.response.PlannerMemberResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupInvitationEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.InvitationStatus;
import com.example.PartTrip.planner.repository.GroupInvitationRepository;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.signup.entity.UserEntity;
import com.example.PartTrip.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerMemberListService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupInvitationRepository groupInvitationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<PlannerMemberResponseDto> getPlannerMembers(
            Long plannerId,
            String userId
    ) {
        if (!travelGroupRepository.existsById(plannerId)) {
            throw new IllegalArgumentException("플래너가 존재하지 않습니다.");
        }

        if (!groupMemberRepository.existsByGroupIdAndUserId(plannerId, userId)) {
            throw new IllegalArgumentException("해당 플래너의 멤버만 조회할 수 있습니다.");
        }

        List<GroupMemberEntity> members = groupMemberRepository
                .findByGroupIdOrderByJoinedAtAsc(plannerId);

        List<GroupInvitationEntity> pendingInvitations = groupInvitationRepository
                .findByGroupIdAndStatusOrderByCreatedAtAsc(plannerId, InvitationStatus.PENDING);
        List<String> relatedUserIds = new ArrayList<>();
        relatedUserIds.addAll(members.stream().map(GroupMemberEntity::getUserId).toList());
        relatedUserIds.addAll(pendingInvitations.stream()
                .map(GroupInvitationEntity::getInvitedUserId).toList());

        Map<String, UserEntity> usersById = userRepository
                .findAllById(relatedUserIds)
                .stream()
                .collect(Collectors.toMap(UserEntity::getUserId, Function.identity()));

        List<PlannerMemberResponseDto> result = new ArrayList<>(members.stream()
                .sorted(Comparator
                        .comparingInt((GroupMemberEntity member) -> roleOrder(member.getRole()))
                        .thenComparing(GroupMemberEntity::getJoinedAt))
                .map(member -> toResponse(member, usersById.get(member.getUserId())))
                .toList());
        result.addAll(pendingInvitations.stream()
                .map(invitation -> toPendingResponse(
                        invitation,
                        usersById.get(invitation.getInvitedUserId())))
                .toList());
        return result;
    }

    private int roleOrder(GroupRole role) {
        return role == GroupRole.OWNER ? 0 : 1;
    }

    private PlannerMemberResponseDto toResponse(
            GroupMemberEntity member,
            UserEntity user
    ) {
        return PlannerMemberResponseDto.builder()
                .userId(member.getUserId())
                .nickName(user == null ? "알 수 없는 사용자" : user.getNickName())
                .role(member.getRole().name())
                .status(member.getRole() == GroupRole.OWNER ? "HOST" : "ACCEPTED")
                .joinedAt(member.getJoinedAt())
                .build();
    }

    private PlannerMemberResponseDto toPendingResponse(
            GroupInvitationEntity invitation,
            UserEntity user
    ) {
        return PlannerMemberResponseDto.builder()
                .invitationId(invitation.getInvitationId())
                .userId(invitation.getInvitedUserId())
                .nickName(user == null ? "알 수 없는 사용자" : user.getNickName())
                .role(GroupRole.MEMBER.name())
                .status(InvitationStatus.PENDING.name())
                .build();
    }
}
