package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.request.PlannerConfirmRequestDto;
import com.example.PartTrip.planner.dto.request.VoteConfirmRequestDto;
import com.example.PartTrip.planner.dto.response.ConfirmedPlaceResponseDto;
import com.example.PartTrip.planner.dto.response.PlannerFinalResponseDto;
import com.example.PartTrip.planner.dto.response.VoteCloseResponseDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.VoteStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.entity.TripCardPlaceEntity;
import com.example.PartTrip.tripcard.repository.TripCardPlaceRepository;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

/**
 * 장바구니(C6)는 아무도 투표하지 않아 모든 후보가 0표 동점이다.
 * 그래서 고른 것을 함께 받지 못하면 확정이 반드시 거부된다.
 */
@ExtendWith(MockitoExtension.class)
class PlannerConfirmServiceTest {

    private static final long PLANNER_ID = 1L;
    private static final long PLAN_ID = 10L;
    private static final long VOTE_ID = 100L;
    private static final long OPTION_A = 1000L;
    private static final long OPTION_B = 1001L;
    private static final String OWNER_ID = "owner";

    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private VoteRepository voteRepository;
    @Mock private VoteConfirmService voteConfirmService;
    @Mock private PlannerFinalService plannerFinalService;
    @Mock private TripCardRepository tripCardRepository;
    @Mock private TripCardPlaceRepository tripCardPlaceRepository;
    @Mock private TourPlaceRepository tourPlaceRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @InjectMocks private PlannerConfirmService plannerConfirmService;

    private VoteEntity vote;
    private GroupTravelPlanEntity plan;

    @BeforeEach
    void setUp() {
        TravelGroupEntity group = new TravelGroupEntity();
        group.setGroupId(PLANNER_ID);
        group.setGroupName("오사카 여행팟");
        group.setOwnerUserId(OWNER_ID);

        GroupMemberEntity owner = owner();

        plan = new GroupTravelPlanEntity();
        plan.setPlanId(PLAN_ID);
        plan.setGroupId(PLANNER_ID);

        vote = new VoteEntity();
        vote.setVoteId(VOTE_ID);
        vote.setPlanId(PLAN_ID);
        vote.setStatus(VoteStatus.OPEN);
        vote.setCategory(TourPlaceCategory.RESTAURANT);

        given(travelGroupRepository.findById(PLANNER_ID)).willReturn(Optional.of(group));
        given(groupMemberRepository.findByGroupIdAndUserId(PLANNER_ID, OWNER_ID))
                .willReturn(Optional.of(owner));
        given(groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(PLANNER_ID))
                .willReturn(Optional.of(plan));
        given(voteRepository.findByPlanId(PLAN_ID)).willReturn(List.of(vote));
    }

    /** 두 후보가 0표로 동점인 상황 */
    private void givenTiedVote() {
        given(voteConfirmService.closeVote(PLANNER_ID, VOTE_ID, OWNER_ID))
                .willReturn(VoteCloseResponseDto.builder()
                        .voteId(VOTE_ID)
                        .topOptionIds(List.of(OPTION_A, OPTION_B))
                        .tied(true)
                        .build());
    }

    /** 확정 뒤 여행 카드까지 만들어지도록 나머지를 세운다 */
    private void givenTripCardCreation() {
        given(plannerFinalService.getConfirmedPlaces(PLANNER_ID, OWNER_ID))
                .willReturn(PlannerFinalResponseDto.builder().places(List.of()).build());
        given(groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(PLANNER_ID))
                .willReturn(List.of(owner()));
        given(tripCardRepository.findByPlanIdAndUserIdIn(eq(PLAN_ID), any()))
                .willReturn(List.of());
        given(tripCardRepository.saveAll(any()))
                .willReturn(List.of(TripCardEntity.builder()
                        .tripCardId(7L).userId(OWNER_ID).planId(PLAN_ID).build()));
        lenient().when(tourPlaceRepository.findAllById(any())).thenReturn(List.of());
    }

    private GroupMemberEntity owner() {
        GroupMemberEntity member = new GroupMemberEntity();
        member.setGroupId(PLANNER_ID);
        member.setUserId(OWNER_ID);
        member.setRole(GroupRole.OWNER);
        return member;
    }

    private PlannerConfirmRequestDto requestOf(Long voteId, Long optionId) {
        PlannerConfirmRequestDto.VoteSelection selection =
                new PlannerConfirmRequestDto.VoteSelection();
        selection.setVoteId(voteId);
        selection.setOptionId(optionId);
        PlannerConfirmRequestDto request = new PlannerConfirmRequestDto();
        request.setSelections(List.of(selection));
        return request;
    }

    @Test
    void 고른_후보를_그대로_확정한다() {
        givenTiedVote();
        givenTripCardCreation();

        plannerConfirmService.confirmPlanner(
                PLANNER_ID, requestOf(VOTE_ID, OPTION_B), OWNER_ID);

        ArgumentCaptor<VoteConfirmRequestDto> captor =
                ArgumentCaptor.forClass(VoteConfirmRequestDto.class);
        verify(voteConfirmService)
                .confirmVote(eq(PLANNER_ID), eq(VOTE_ID), captor.capture(), eq(OWNER_ID));
        assertThat(captor.getValue().getOptionId()).isEqualTo(OPTION_B);
    }

    @Test
    void 고른_것이_없고_동점이면_거부한다() {
        givenTiedVote();

        assertThatThrownBy(() ->
                plannerConfirmService.confirmPlanner(PLANNER_ID, null, OWNER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("맛집")
                .hasMessageContaining("동점")
                // voteId 같은 내부 번호를 사용자에게 보여주지 않는다
                .hasMessageNotContaining("voteId");
    }

    @Test
    void 다른_플래너의_투표는_거부한다() {
        assertThatThrownBy(() ->
                plannerConfirmService.confirmPlanner(
                        PLANNER_ID, requestOf(999L, OPTION_A), OWNER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이 플래너의 투표가 아닙니다");
    }

    @Test
    void 한_투표에_두_후보를_보내면_거부한다() {
        PlannerConfirmRequestDto.VoteSelection first =
                new PlannerConfirmRequestDto.VoteSelection();
        first.setVoteId(VOTE_ID);
        first.setOptionId(OPTION_A);
        PlannerConfirmRequestDto.VoteSelection second =
                new PlannerConfirmRequestDto.VoteSelection();
        second.setVoteId(VOTE_ID);
        second.setOptionId(OPTION_B);
        PlannerConfirmRequestDto request = new PlannerConfirmRequestDto();
        request.setSelections(List.of(first, second));

        assertThatThrownBy(() ->
                plannerConfirmService.confirmPlanner(PLANNER_ID, request, OWNER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("한 투표에는 하나만");
    }

    @Test
    void 같은_후보를_두_번_보내도_거부한다() {
        PlannerConfirmRequestDto.VoteSelection first =
                new PlannerConfirmRequestDto.VoteSelection();
        first.setVoteId(VOTE_ID);
        first.setOptionId(OPTION_A);
        PlannerConfirmRequestDto.VoteSelection second =
                new PlannerConfirmRequestDto.VoteSelection();
        second.setVoteId(VOTE_ID);
        second.setOptionId(OPTION_A);
        PlannerConfirmRequestDto request = new PlannerConfirmRequestDto();
        request.setSelections(List.of(first, second));

        assertThatThrownBy(() ->
                plannerConfirmService.confirmPlanner(PLANNER_ID, request, OWNER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("한 투표에는 하나만");
    }

    @Test
    void 선택_항목이_null_이면_거부한다() {
        PlannerConfirmRequestDto request = new PlannerConfirmRequestDto();
        // List.of 는 null 을 못 담는다. JSON 으로는 [null] 이 들어온다.
        request.setSelections(java.util.Collections.singletonList(null));

        assertThatThrownBy(() ->
                plannerConfirmService.confirmPlanner(PLANNER_ID, request, OWNER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("선택 항목이 비어 있습니다");
    }

    @Test
    void 이미_확정된_투표는_다시_확정하지_않는다() {
        vote.setStatus(VoteStatus.CONFIRMED);
        givenTripCardCreation();

        plannerConfirmService.confirmPlanner(
                PLANNER_ID, requestOf(VOTE_ID, OPTION_A), OWNER_ID);

        verify(voteConfirmService, org.mockito.Mockito.never())
                .closeVote(anyLong(), anyLong(), anyString());
        verify(voteConfirmService, org.mockito.Mockito.never())
                .confirmVote(anyLong(), anyLong(), any(), anyString());
    }

    @Test
    void 숙소는_전_날짜에_맛집은_하루_두_곳씩_배치한다() {
        plan.setStartDate(LocalDate.of(2026, 9, 1));
        plan.setEndDate(LocalDate.of(2026, 9, 2));
        vote.setStatus(VoteStatus.CONFIRMED);
        givenTripCardCreation();
        List<ConfirmedPlaceResponseDto> places = List.of(
                confirmed("ACCOMMODATION", 1L),
                confirmed("RESTAURANT", 2L),
                confirmed("RESTAURANT", 3L),
                confirmed("RESTAURANT", 4L));
        given(plannerFinalService.getConfirmedPlaces(PLANNER_ID, OWNER_ID))
                .willReturn(PlannerFinalResponseDto.builder().places(places).build());

        plannerConfirmService.confirmPlanner(PLANNER_ID, null, OWNER_ID);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<TripCardPlaceEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(tripCardPlaceRepository).saveAll(captor.capture());
        assertThat(captor.getValue())
                .extracting(TripCardPlaceEntity::getTourPlaceId,
                        TripCardPlaceEntity::getVisitedDate)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple(1L, LocalDate.of(2026, 9, 1)),
                        org.assertj.core.groups.Tuple.tuple(1L, LocalDate.of(2026, 9, 2)),
                        org.assertj.core.groups.Tuple.tuple(2L, LocalDate.of(2026, 9, 1)),
                        org.assertj.core.groups.Tuple.tuple(3L, LocalDate.of(2026, 9, 1)),
                        org.assertj.core.groups.Tuple.tuple(4L, LocalDate.of(2026, 9, 2)));
    }

    private ConfirmedPlaceResponseDto confirmed(String category, Long tourPlaceId) {
        return ConfirmedPlaceResponseDto.builder()
                .category(category)
                .tourPlaceId(tourPlaceId)
                .placeName("장소 " + tourPlaceId)
                .build();
    }
}
