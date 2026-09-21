package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.planner.dto.response.ConfirmedPlaceResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerFinalResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.entity.TripCardPlaceEntity;
import com.example.PartTrip.tripcard.repository.TripCardPlaceRepository;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * 투표를 없앤 뒤(#161)의 확정: 일정 생성기가 채운 일정으로 멤버마다 여행 카드를
 * 만들고 플래너를 확정 상태로 바꾼다.
 */
@ExtendWith(MockitoExtension.class)
class PlannerConfirmServiceTest {

    private static final long PLANNER_ID = 1L;
    private static final long PLAN_ID = 10L;
    private static final String OWNER_ID = "owner";
    private static final LocalDate DAY1 = LocalDate.of(2026, 9, 1);
    private static final LocalDate DAY2 = LocalDate.of(2026, 9, 2);

    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private PlannerFinalService plannerFinalService;
    @Mock private TripCardRepository tripCardRepository;
    @Mock private TripCardPlaceRepository tripCardPlaceRepository;
    @Mock private PlannerScheduleService plannerScheduleService;
    @Mock private ApplicationEventPublisher eventPublisher;
    @InjectMocks private PlannerConfirmService plannerConfirmService;

    private TravelGroupEntity group;
    private GroupTravelPlanEntity plan;

    @BeforeEach
    void setUp() {
        group = new TravelGroupEntity();
        group.setGroupId(PLANNER_ID);
        group.setGroupName("강릉 여행");
        group.setOwnerUserId(OWNER_ID);
        group.setStatus(GroupStatus.PLANNING);

        plan = new GroupTravelPlanEntity();
        plan.setPlanId(PLAN_ID);
        plan.setGroupId(PLANNER_ID);
        plan.setStartDate(DAY1);
        plan.setEndDate(DAY2);

        given(travelGroupRepository.findById(PLANNER_ID)).willReturn(Optional.of(group));
        lenient().when(groupMemberRepository.findByGroupIdAndUserId(PLANNER_ID, OWNER_ID))
                .thenReturn(Optional.of(member(OWNER_ID, GroupRole.OWNER)));
        lenient().when(groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(PLANNER_ID))
                .thenReturn(Optional.of(plan));
        lenient().when(groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(PLANNER_ID))
                .thenReturn(List.of(member(OWNER_ID, GroupRole.OWNER)));
        lenient().when(plannerFinalService.getConfirmedPlaces(PLANNER_ID, OWNER_ID))
                .thenReturn(PlannerFinalResponseDto.builder().places(List.of()).build());
    }

    private GroupMemberEntity member(String userId, GroupRole role) {
        GroupMemberEntity member = new GroupMemberEntity();
        member.setGroupId(PLANNER_ID);
        member.setUserId(userId);
        member.setRole(role);
        return member;
    }

    private PlannerScheduleService.ScheduledPlace scheduled(long id, LocalDate date, int order) {
        TourPlaceEntity place = new TourPlaceEntity();
        place.setTourPlaceId(id);
        ConfirmedPlaceResponseDto dto = ConfirmedPlaceResponseDto.builder()
                .tourPlaceId(id)
                .placeName("장소 " + id)
                .build();
        return new PlannerScheduleService.ScheduledPlace(dto, place, date, order);
    }

    private void givenNoCardsYet() {
        given(tripCardRepository.findByPlanIdAndUserIdIn(eq(PLAN_ID), any())).willReturn(List.of());
        given(tripCardRepository.saveAll(any())).willReturn(List.of(TripCardEntity.builder()
                .tripCardId(7L).userId(OWNER_ID).planId(PLAN_ID).build()));
    }

    @Test
    void 일정대로_여행카드에_날짜별로_저장하고_확정한다() {
        givenNoCardsYet();
        // 확정할 투표가 없으니 일정 생성기에는 빈 목록을 넘긴다. 생성기가 추천으로 채운다
        given(plannerScheduleService.buildSchedule(plan, List.of(), OWNER_ID)).willReturn(List.of(
                scheduled(1L, DAY1, 1),
                scheduled(2L, DAY1, 2),
                scheduled(3L, DAY2, 1)));

        var result = plannerConfirmService.confirmPlanner(PLANNER_ID, OWNER_ID);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<TripCardPlaceEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(tripCardPlaceRepository).saveAll(captor.capture());
        assertThat(captor.getValue())
                .extracting(TripCardPlaceEntity::getTourPlaceId,
                        TripCardPlaceEntity::getVisitedDate,
                        TripCardPlaceEntity::getSortOrder)
                .containsExactly(
                        Tuple.tuple(1L, DAY1, 1),
                        Tuple.tuple(2L, DAY1, 2),
                        Tuple.tuple(3L, DAY2, 1));
        assertThat(group.getStatus()).isEqualTo(GroupStatus.CONFIRMED);
        assertThat(result.getTripCardId()).isEqualTo(7L);
    }

    @Test
    void 그룹장이_아니면_거부한다() {
        given(groupMemberRepository.findByGroupIdAndUserId(PLANNER_ID, "member"))
                .willReturn(Optional.of(member("member", GroupRole.MEMBER)));

        assertThatThrownBy(() -> plannerConfirmService.confirmPlanner(PLANNER_ID, "member"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("그룹장");
        assertThat(group.getStatus()).isEqualTo(GroupStatus.PLANNING);
    }

    @Test
    void 넣을_장소가_하나도_없으면_거부한다() {
        given(tripCardRepository.findByPlanIdAndUserIdIn(eq(PLAN_ID), any())).willReturn(List.of());
        given(plannerScheduleService.buildSchedule(plan, List.of(), OWNER_ID)).willReturn(List.of());

        // 빈 여행 카드가 만들어지고 확정되면 되돌릴 방법이 없다
        assertThatThrownBy(() -> plannerConfirmService.confirmPlanner(PLANNER_ID, OWNER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("장소가 없습니다");
        verify(tripCardRepository, never()).saveAll(any());
        assertThat(group.getStatus()).isEqualTo(GroupStatus.PLANNING);
    }

    @Test
    void 이미_카드가_있으면_다시_만들지_않는다() {
        given(tripCardRepository.findByPlanIdAndUserIdIn(eq(PLAN_ID), any()))
                .willReturn(List.of(TripCardEntity.builder()
                        .tripCardId(7L).userId(OWNER_ID).planId(PLAN_ID).build()));
        given(plannerScheduleService.buildSchedule(plan, List.of(), OWNER_ID))
                .willReturn(List.of(scheduled(1L, DAY1, 1)));
        given(tripCardRepository.saveAll(List.of())).willReturn(List.of());

        var result = plannerConfirmService.confirmPlanner(PLANNER_ID, OWNER_ID);

        verify(tripCardPlaceRepository).saveAll(List.of());
        assertThat(result.getTripCardId()).isEqualTo(7L);
    }
}
