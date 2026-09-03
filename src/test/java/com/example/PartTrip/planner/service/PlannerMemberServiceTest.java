package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.request.JoinPlannerRequestDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlannerMemberServiceTest {

    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private PlannerScheduleLockService plannerScheduleLockService;
    @InjectMocks private PlannerMemberService plannerMemberService;

    @Test
    void 계획중이_아닌_플래너에는_참여할_수_없다() {
        JoinPlannerRequestDto request = new JoinPlannerRequestDto();
        request.setInviteCode("invite");
        TravelGroupEntity group = new TravelGroupEntity();
        group.setStatus(GroupStatus.VOTING);
        when(travelGroupRepository.findByInviteCodeForUpdate("INVITE"))
                .thenReturn(Optional.of(group));

        assertThatThrownBy(() -> plannerMemberService.joinPlanner(request, "user"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("계획 중");

        verifyNoInteractions(groupMemberRepository, eventPublisher);
    }

    @Test
    void 기간이_겹치는_플래너에는_초대코드로_참여할_수_없다() {
        JoinPlannerRequestDto request = new JoinPlannerRequestDto();
        request.setInviteCode("invite");
        TravelGroupEntity group = new TravelGroupEntity();
        group.setGroupId(1L);
        group.setStatus(GroupStatus.PLANNING);
        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setStartDate(LocalDate.of(2026, 9, 1));
        plan.setEndDate(LocalDate.of(2026, 9, 30));

        when(travelGroupRepository.findByInviteCodeForUpdate("INVITE"))
                .thenReturn(Optional.of(group));
        when(groupMemberRepository.existsByGroupIdAndUserId(1L, "user")).thenReturn(false);
        when(groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.of(plan));
        when(groupTravelPlanRepository.existsOverlappingPlanForUser(
                "user", plan.getStartDate(), plan.getEndDate())).thenReturn(true);

        assertThatThrownBy(() -> plannerMemberService.joinPlanner(request, "user"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 기간에 이미 등록된 여행 계획이 있습니다.");

        verify(groupMemberRepository, never()).save(any());
        verifyNoInteractions(eventPublisher);
    }
}
