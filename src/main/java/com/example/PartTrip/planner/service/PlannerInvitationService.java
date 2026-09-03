package com.example.PartTrip.planner.service;

import com.example.PartTrip.notification.event.GroupInviteAcceptedEvent;
import com.example.PartTrip.notification.event.GroupInvitedEvent;
import com.example.PartTrip.planner.dto.request.InvitePlannerMembersRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerInvitationResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerInviteResponseDto;
import com.example.PartTrip.planner.entity.GroupInvitationEntity;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.InvitationStatus;
import com.example.PartTrip.planner.repository.GroupInvitationRepository;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerInvitationService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final GroupInvitationRepository groupInvitationRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PlannerInviteLinkFactory inviteLinkFactory;

    @Transactional
    public PlannerInviteResponseDto inviteMembers(
            Long plannerId,
            InvitePlannerMembersRequestDto dto,
            String requesterUserId
    ) {
        TravelGroupEntity group = travelGroupRepository.findByIdForUpdate(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));
        requireOwner(plannerId, requesterUserId);

        List<String> userIds = dto.getUserIds().stream()
                .filter(id -> id != null && !id.isBlank())
                .map(String::trim)
                .distinct()
                .toList();
        if (userIds.isEmpty()) {
            throw new IllegalArgumentException("초대할 사용자를 한 명 이상 입력해주세요.");
        }
        if (userIds.contains(requesterUserId)) {
            throw new IllegalArgumentException("본인은 초대할 수 없습니다.");
        }

        long memberCount = groupMemberRepository.countByGroupId(plannerId);
        long pendingCount = groupInvitationRepository
                .countByGroupIdAndStatus(plannerId, InvitationStatus.PENDING);
        if (memberCount + pendingCount + userIds.size() > group.getHeadcount()) {
            throw new IllegalArgumentException("설정한 여행 인원을 초과하여 초대할 수 없습니다.");
        }

        Set<String> existingUserIds = userRepository.findAllById(userIds).stream()
                .map(user -> user.getUserId())
                .collect(Collectors.toSet());
        Set<String> memberUserIds = groupMemberRepository
                .findByGroupIdAndUserIdIn(plannerId, userIds).stream()
                .map(GroupMemberEntity::getUserId)
                .collect(Collectors.toSet());
        Map<String, GroupInvitationEntity> invitationsByUserId = groupInvitationRepository
                .findByGroupIdAndInvitedUserIdIn(plannerId, userIds).stream()
                .collect(Collectors.toMap(
                        GroupInvitationEntity::getInvitedUserId,
                        Function.identity()
                ));

        LocalDateTime now = LocalDateTime.now();
        List<GroupInvitationEntity> invitationsToSave = new ArrayList<>();
        for (String invitedUserId : userIds) {
            if (!existingUserIds.contains(invitedUserId)) {
                throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + invitedUserId);
            }
            if (memberUserIds.contains(invitedUserId)) {
                throw new IllegalArgumentException("이미 참여 중인 사용자입니다: " + invitedUserId);
            }

            GroupInvitationEntity invitation = invitationsByUserId
                    .getOrDefault(invitedUserId, new GroupInvitationEntity());
            if (invitation.getStatus() == InvitationStatus.PENDING) {
                throw new IllegalArgumentException("이미 초대 대기 중인 사용자입니다: " + invitedUserId);
            }

            invitation.setGroupId(plannerId);
            invitation.setInvitedUserId(invitedUserId);
            invitation.setInvitedByUserId(requesterUserId);
            invitation.setStatus(InvitationStatus.PENDING);
            invitation.setCreatedAt(now);
            invitation.setRespondedAt(null);
            invitationsToSave.add(invitation);
        }

        List<GroupInvitationEntity> savedInvitations = groupInvitationRepository.saveAll(invitationsToSave);
        List<PlannerInvitationResponseDto> responses = savedInvitations.stream()
                .map(invitation -> toResponse(group, invitation))
                .toList();
        for (GroupInvitationEntity saved : savedInvitations) {
            eventPublisher.publishEvent(
                    new GroupInvitedEvent(plannerId, saved.getInvitedUserId(), requesterUserId));
        }

        return PlannerInviteResponseDto.builder()
                .inviteLink(inviteLinkFactory.create(group.getInviteCode()))
                .invitedCount(responses.size())
                .invitations(responses)
                .build();
    }

    @Transactional(readOnly = true)
    public List<PlannerInvitationResponseDto> getMyPendingInvitations(String userId) {
        List<GroupInvitationEntity> invitations = groupInvitationRepository
                .findByInvitedUserIdAndStatusOrderByCreatedAtDesc(userId, InvitationStatus.PENDING);
        Map<Long, TravelGroupEntity> groupsById = travelGroupRepository
                .findAllById(invitations.stream().map(GroupInvitationEntity::getGroupId).distinct().toList())
                .stream()
                .collect(Collectors.toMap(TravelGroupEntity::getGroupId, Function.identity()));
        return invitations.stream()
                .map(invitation -> toResponse(
                        java.util.Optional.ofNullable(groupsById.get(invitation.getGroupId()))
                                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다.")),
                        invitation
                ))
                .toList();
    }

    @Transactional
    public PlannerInvitationResponseDto acceptInvitation(Long invitationId, String userId) {
        GroupInvitationEntity invitation = getPendingInvitationForUser(invitationId, userId);
        TravelGroupEntity group = travelGroupRepository.findByIdForUpdate(invitation.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));

        if (groupMemberRepository.existsByGroupIdAndUserId(group.getGroupId(), userId)) {
            throw new IllegalArgumentException("이미 참여한 플래너입니다.");
        }
        validateNoOverlappingPlan(group.getGroupId(), userId);
        if (groupMemberRepository.countByGroupId(group.getGroupId()) >= group.getHeadcount()) {
            throw new IllegalArgumentException("참여 가능한 인원이 모두 찼습니다.");
        }

        GroupMemberEntity member = new GroupMemberEntity();
        member.setGroupId(group.getGroupId());
        member.setUserId(userId);
        member.setRole(GroupRole.MEMBER);
        member.setJoinedAt(LocalDateTime.now());
        groupMemberRepository.save(member);

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRespondedAt(LocalDateTime.now());
        groupInvitationRepository.save(invitation);
        eventPublisher.publishEvent(new GroupInviteAcceptedEvent(group.getGroupId(), userId));
        return toResponse(group, invitation);
    }

    private void validateNoOverlappingPlan(Long groupId, String userId) {
        groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(groupId)
                .filter(plan -> groupTravelPlanRepository.existsOverlappingPlanForUser(
                        userId, plan.getStartDate(), plan.getEndDate()))
                .ifPresent(plan -> {
                    throw new IllegalArgumentException("해당 기간에 이미 등록된 여행 계획이 있습니다.");
                });
    }

    @Transactional
    public PlannerInvitationResponseDto rejectInvitation(Long invitationId, String userId) {
        GroupInvitationEntity invitation = getPendingInvitationForUser(invitationId, userId);
        invitation.setStatus(InvitationStatus.REJECTED);
        invitation.setRespondedAt(LocalDateTime.now());
        TravelGroupEntity group = travelGroupRepository.findById(invitation.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("플래너가 존재하지 않습니다."));
        return toResponse(group, groupInvitationRepository.save(invitation));
    }

    @Transactional
    public void cancelInvitation(Long plannerId, Long invitationId, String requesterUserId) {
        requireOwner(plannerId, requesterUserId);
        GroupInvitationEntity invitation = groupInvitationRepository.findByIdForUpdate(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("초대 내역이 존재하지 않습니다."));
        if (!plannerId.equals(invitation.getGroupId())) {
            throw new IllegalArgumentException("해당 플래너의 초대가 아닙니다.");
        }
        requirePending(invitation);
        invitation.setStatus(InvitationStatus.CANCELED);
        invitation.setRespondedAt(LocalDateTime.now());
    }

    @Transactional
    public void removeMember(Long plannerId, String memberUserId, String requesterUserId) {
        requireOwner(plannerId, requesterUserId);
        GroupMemberEntity member = groupMemberRepository
                .findByGroupIdAndUserId(plannerId, memberUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 플래너 멤버가 아닙니다."));
        if (member.getRole() == GroupRole.OWNER) {
            throw new IllegalArgumentException("플래너 그룹장은 내보낼 수 없습니다.");
        }
        groupMemberRepository.delete(member);
    }

    private GroupInvitationEntity getPendingInvitationForUser(Long invitationId, String userId) {
        GroupInvitationEntity invitation = groupInvitationRepository.findByIdForUpdate(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("초대 내역이 존재하지 않습니다."));
        if (!userId.equals(invitation.getInvitedUserId())) {
            throw new IllegalArgumentException("본인에게 온 초대만 응답할 수 있습니다.");
        }
        requirePending(invitation);
        return invitation;
    }

    private void requirePending(GroupInvitationEntity invitation) {
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new IllegalArgumentException("이미 처리된 초대입니다.");
        }
    }

    private void requireOwner(Long plannerId, String userId) {
        GroupMemberEntity member = groupMemberRepository
                .findByGroupIdAndUserId(plannerId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플래너의 멤버가 아닙니다."));
        if (member.getRole() != GroupRole.OWNER) {
            throw new IllegalArgumentException("플래너 그룹장만 수행할 수 있습니다.");
        }
    }

    private PlannerInvitationResponseDto toResponse(
            TravelGroupEntity group,
            GroupInvitationEntity invitation
    ) {
        return PlannerInvitationResponseDto.builder()
                .invitationId(invitation.getInvitationId())
                .plannerId(group.getGroupId())
                .plannerTitle(group.getGroupName())
                .invitedUserId(invitation.getInvitedUserId())
                .invitedByUserId(invitation.getInvitedByUserId())
                .status(invitation.getStatus().name())
                .createdAt(invitation.getCreatedAt())
                .respondedAt(invitation.getRespondedAt())
                .build();
    }
}
