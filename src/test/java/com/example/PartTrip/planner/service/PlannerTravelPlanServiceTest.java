package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.dto.request.SavePlannerTravelPlanRequestDto;
import com.example.PartTrip.planner.dto.response.PlannerTravelPlanResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlannerTravelPlanServiceTest {

    private static final long PLANNER_ID = 1L;
    private static final String OWNER_ID = "owner";

    @Mock
    private TravelGroupRepository travelGroupRepository;
    @Mock
    private GroupMemberRepository groupMemberRepository;
    @Mock
    private GroupTravelPlanRepository groupTravelPlanRepository;
    @InjectMocks
    private PlannerTravelPlanService plannerTravelPlanService;

    private TravelGroupEntity group;

    @BeforeEach
    void setUp() {
        group = new TravelGroupEntity();
        group.setGroupId(PLANNER_ID);
        group.setGroupName("도쿄 여행");
        group.setHeadcount(4);
        group.setStatus(GroupStatus.PLANNING);

        GroupMemberEntity owner = new GroupMemberEntity();
        owner.setGroupId(PLANNER_ID);
        owner.setUserId(OWNER_ID);
        owner.setRole(GroupRole.OWNER);

        given(travelGroupRepository.findById(PLANNER_ID)).willReturn(Optional.of(group));
        given(groupMemberRepository.findByGroupIdAndUserId(PLANNER_ID, OWNER_ID))
                .willReturn(Optional.of(owner));
    }

    @Test
    void updatesMemberCountWhenItIsNotLessThanJoinedMembers() {
        SavePlannerTravelPlanRequestDto request = request();
        request.setMemberCount(3);
        request.setIsSolo(false);

        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setPlanId(10L);
        plan.setGroupId(PLANNER_ID);
        plan.setTravelTitle("도쿄 여행");

        given(groupMemberRepository.countByGroupId(PLANNER_ID)).willReturn(2L);
        given(groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(PLANNER_ID))
                .willReturn(Optional.of(plan));
        given(groupTravelPlanRepository.save(any(GroupTravelPlanEntity.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        PlannerTravelPlanResponseDto response = plannerTravelPlanService
                .saveTravelPlan(PLANNER_ID, request, OWNER_ID);

        assertThat(group.getHeadcount()).isEqualTo(3);
        assertThat(response.getMemberCount()).isEqualTo(3);
        assertThat(response.getIsSolo()).isFalse();
    }

    @Test
    void rejectsMemberCountBelowCurrentJoinedMembers() {
        SavePlannerTravelPlanRequestDto request = request();
        request.setMemberCount(2);
        given(groupMemberRepository.countByGroupId(PLANNER_ID)).willReturn(3L);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> plannerTravelPlanService.saveTravelPlan(PLANNER_ID, request, OWNER_ID))
                .withMessage("여행 인원은 현재 참여 멤버 수보다 적을 수 없습니다.");
        verify(groupTravelPlanRepository, never()).save(any());
    }

    @Test
    void rejectsSoloWhenAnotherMemberHasAlreadyJoined() {
        SavePlannerTravelPlanRequestDto request = request();
        request.setIsSolo(true);
        given(groupMemberRepository.countByGroupId(PLANNER_ID)).willReturn(2L);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> plannerTravelPlanService.saveTravelPlan(PLANNER_ID, request, OWNER_ID))
                .withMessage("이미 참여한 멤버가 있어 혼자 여행으로 변경할 수 없습니다.");
        verify(groupTravelPlanRepository, never()).save(any());
    }

    private SavePlannerTravelPlanRequestDto request() {
        SavePlannerTravelPlanRequestDto request = new SavePlannerTravelPlanRequestDto();
        request.setCountryName("일본");
        request.setCityName("도쿄");
        request.setStartDate(LocalDate.of(2026, 9, 1));
        request.setEndDate(LocalDate.of(2026, 9, 5));
        return request;
    }
}
