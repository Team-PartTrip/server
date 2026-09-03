package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.request.InvitePlannerMembersRequestDto;
import com.example.PartTrip.planner.entity.GroupInvitationEntity;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.InvitationStatus;
import com.example.PartTrip.planner.repository.GroupInvitationRepository;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.signup.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlannerInvitationServiceTest {

    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private GroupInvitationRepository groupInvitationRepository;
    @Mock private UserRepository userRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private PlannerInviteLinkFactory inviteLinkFactory;
    @Mock private PlannerScheduleLockService plannerScheduleLockService;
    @InjectMocks private PlannerInvitationService plannerInvitationService;

    @Test
    void 인원초과는_사용자와_초대내역을_조회하기_전에_거부한다() {
        TravelGroupEntity group = new TravelGroupEntity();
        group.setHeadcount(2);
        GroupMemberEntity owner = new GroupMemberEntity();
        owner.setRole(GroupRole.OWNER);
        InvitePlannerMembersRequestDto request = new InvitePlannerMembersRequestDto();
        request.setUserIds(List.of("new-member"));

        when(travelGroupRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(group));
        when(groupMemberRepository.findByGroupIdAndUserId(1L, "owner"))
                .thenReturn(Optional.of(owner));
        when(groupMemberRepository.countByGroupId(1L)).thenReturn(1L);
        when(groupInvitationRepository.countByGroupIdAndStatus(1L, InvitationStatus.PENDING))
                .thenReturn(1L);

        assertThatThrownBy(() -> plannerInvitationService.inviteMembers(1L, request, "owner"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("인원을 초과");

        verify(userRepository, never()).findAllById(List.of("new-member"));
        verify(groupInvitationRepository, never())
                .findByGroupIdAndInvitedUserIdIn(1L, List.of("new-member"));
    }

    @Test
    void 기간이_겹치는_플래너_초대는_수락할_수_없다() {
        GroupInvitationEntity invitation = new GroupInvitationEntity();
        invitation.setInvitationId(10L);
        invitation.setGroupId(1L);
        invitation.setInvitedUserId("user");
        invitation.setStatus(InvitationStatus.PENDING);
        TravelGroupEntity group = new TravelGroupEntity();
        group.setGroupId(1L);
        group.setHeadcount(2);
        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setStartDate(LocalDate.of(2026, 9, 1));
        plan.setEndDate(LocalDate.of(2026, 9, 30));

        when(groupInvitationRepository.findByIdForUpdate(10L))
                .thenReturn(Optional.of(invitation));
        when(travelGroupRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(group));
        when(groupMemberRepository.existsByGroupIdAndUserId(1L, "user")).thenReturn(false);
        when(groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.of(plan));
        when(groupTravelPlanRepository.existsOverlappingPlanForUser(
                "user", plan.getStartDate(), plan.getEndDate())).thenReturn(true);

        assertThatThrownBy(() -> plannerInvitationService.acceptInvitation(10L, "user"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 기간에 이미 등록된 여행 계획이 있습니다.");

        verify(groupMemberRepository, never()).save(any());
        verify(groupInvitationRepository, never()).save(any());
    }
}
