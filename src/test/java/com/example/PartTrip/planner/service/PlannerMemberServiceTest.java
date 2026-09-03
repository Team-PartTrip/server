package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.request.JoinPlannerRequestDto;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlannerMemberServiceTest {

    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
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
}
