package com.example.PartTrip.planner.service;

import com.example.PartTrip.global.exception.ForbiddenException;
import com.example.PartTrip.global.exception.NotFoundException;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.repository.GroupInvitationRepository;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteOptionRepository;
import com.example.PartTrip.planner.repository.VoteRecordRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

// 외래키가 없어서 순서를 틀리면 고아 행이 남는다. 순서를 여기서 잡는다.
@ExtendWith(MockitoExtension.class)
class PlannerDeleteServiceTest {

    private static final long PLANNER_ID = 1L;
    private static final long PLAN_ID = 10L;
    private static final long VOTE_ID = 100L;
    private static final String OWNER_ID = "owner";
    private static final String MEMBER_ID = "member";

    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupInvitationRepository groupInvitationRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private VoteRepository voteRepository;
    @Mock private VoteOptionRepository voteOptionRepository;
    @Mock private VoteRecordRepository voteRecordRepository;
    @Mock private TripCardRepository tripCardRepository;
    @InjectMocks private PlannerDeleteService plannerDeleteService;

    @BeforeEach
    void setUp() {
        lenient().when(travelGroupRepository.existsById(PLANNER_ID)).thenReturn(true);
    }

    private void givenRole(String userId, GroupRole role) {
        GroupMemberEntity member = new GroupMemberEntity();
        member.setRole(role);
        given(groupMemberRepository.findByGroupIdAndUserId(PLANNER_ID, userId))
                .willReturn(Optional.of(member));
    }

    private void givenOnePlanWithOneVote() {
        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setPlanId(PLAN_ID);
        given(groupTravelPlanRepository.findByGroupIdOrderByStartDateDesc(PLANNER_ID))
                .willReturn(List.of(plan));
        VoteEntity vote = new VoteEntity();
        vote.setVoteId(VOTE_ID);
        given(voteRepository.findByPlanIdIn(List.of(PLAN_ID))).willReturn(List.of(vote));
    }

    @Test
    void 자식부터_순서대로_지운다() {
        givenRole(OWNER_ID, GroupRole.OWNER);
        givenOnePlanWithOneVote();
        given(tripCardRepository.findByPlanIdIn(List.of(PLAN_ID))).willReturn(List.of());

        plannerDeleteService.deletePlanner(PLANNER_ID, OWNER_ID);

        InOrder order = inOrder(voteRecordRepository, voteOptionRepository,
                voteRepository, groupTravelPlanRepository,
                groupInvitationRepository, groupMemberRepository, travelGroupRepository);
        order.verify(voteRecordRepository).deleteByVoteIdIn(List.of(VOTE_ID));
        order.verify(voteOptionRepository).deleteByVoteIdIn(List.of(VOTE_ID));
        order.verify(voteRepository).deleteByPlanIdIn(List.of(PLAN_ID));
        order.verify(groupTravelPlanRepository).deleteByGroupId(PLANNER_ID);
        order.verify(groupInvitationRepository).deleteByGroupId(PLANNER_ID);
        order.verify(groupMemberRepository).deleteByGroupId(PLANNER_ID);
        order.verify(travelGroupRepository).deleteById(PLANNER_ID);
    }

    @Test
    void 여행카드는_지우지_않고_연결만_끊는다() {
        givenRole(OWNER_ID, GroupRole.OWNER);
        givenOnePlanWithOneVote();
        // 기본 생성자가 protected 라 빌더로 만든다
        TripCardEntity card = TripCardEntity.builder().planId(PLAN_ID).build();
        given(tripCardRepository.findByPlanIdIn(List.of(PLAN_ID))).willReturn(List.of(card));

        plannerDeleteService.deletePlanner(PLANNER_ID, OWNER_ID);

        // 여행 기록은 사용자의 자산이라 플래너를 지워도 남는다
        assertThat(card.getPlanId()).isNull();
        verify(tripCardRepository, never()).delete(card);
        verify(tripCardRepository, never()).deleteAll(List.of(card));
    }

    @Test
    void 그룹장이_아니면_거부한다() {
        givenRole(MEMBER_ID, GroupRole.MEMBER);

        // 403 으로 나가야 한다 (API-005-12)
        assertThatThrownBy(() -> plannerDeleteService.deletePlanner(PLANNER_ID, MEMBER_ID))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("그룹장");
        verify(travelGroupRepository, never()).deleteById(PLANNER_ID);
    }

    @Test
    void 없는_플래너면_거부한다() {
        given(travelGroupRepository.existsById(PLANNER_ID)).willReturn(false);

        // 404 로 나가야 한다 (API-005-12)
        assertThatThrownBy(() -> plannerDeleteService.deletePlanner(PLANNER_ID, OWNER_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("존재하지 않습니다");
        verify(travelGroupRepository, never()).deleteById(PLANNER_ID);
    }
}
