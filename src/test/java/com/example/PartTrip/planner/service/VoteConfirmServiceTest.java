package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.planner.dto.request.VoteConfirmRequestDto;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.entity.VoteOptionEntity;
import com.example.PartTrip.planner.enums.GroupRole;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.enums.VoteStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteOptionRepository;
import com.example.PartTrip.planner.repository.VoteRecordRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class VoteConfirmServiceTest {

    private static final long PLANNER_ID = 1L;
    private static final long PLAN_ID = 10L;
    private static final long VOTE_ID = 100L;
    private static final String OWNER_ID = "owner";

    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private VoteRepository voteRepository;
    @Mock private VoteOptionRepository voteOptionRepository;
    @Mock private VoteRecordRepository voteRecordRepository;
    @InjectMocks private VoteConfirmService service;

    private TravelGroupEntity group;
    private GroupTravelPlanEntity plan;
    private VoteEntity vote;

    @BeforeEach
    void setUp() {
        group = new TravelGroupEntity();
        group.setGroupId(PLANNER_ID);
        group.setOwnerUserId(OWNER_ID);
        group.setStatus(GroupStatus.VOTING);

        GroupMemberEntity owner = new GroupMemberEntity();
        owner.setGroupId(PLANNER_ID);
        owner.setUserId(OWNER_ID);
        owner.setRole(GroupRole.OWNER);

        plan = new GroupTravelPlanEntity();
        plan.setPlanId(PLAN_ID);
        plan.setGroupId(PLANNER_ID);
        plan.setStartDate(LocalDate.of(2026, 9, 1));
        plan.setEndDate(LocalDate.of(2026, 9, 3));

        vote = new VoteEntity();
        vote.setVoteId(VOTE_ID);
        vote.setPlanId(PLAN_ID);
        vote.setCategory(TourPlaceCategory.RESTAURANT);
        vote.setStatus(VoteStatus.CLOSED);

        given(travelGroupRepository.findById(PLANNER_ID)).willReturn(Optional.of(group));
        given(groupMemberRepository.findByGroupIdAndUserId(PLANNER_ID, OWNER_ID))
                .willReturn(Optional.of(owner));
        given(voteRepository.findByVoteIdForUpdate(VOTE_ID)).willReturn(Optional.of(vote));
        given(groupTravelPlanRepository.findByPlanIdAndGroupId(PLAN_ID, PLANNER_ID))
                .willReturn(Optional.of(plan));
        given(groupTravelPlanRepository.findById(PLAN_ID)).willReturn(Optional.of(plan));
        lenient().when(voteRepository.findByPlanId(PLAN_ID)).thenReturn(List.of(vote));
    }

    @Test
    void 삼일_맛집은_득표순_여섯_곳을_확정한다() {
        List<VoteOptionEntity> options = options(7);
        given(voteOptionRepository.findByVoteIdOrderByCreatedAtAsc(VOTE_ID))
                .willReturn(options);
        given(voteRecordRepository.countByOption(VOTE_ID)).willReturn(List.of(
                row(1, 7), row(2, 6), row(3, 5), row(4, 4),
                row(5, 3), row(6, 2), row(7, 1)));

        service.confirmVote(PLANNER_ID, VOTE_ID, new VoteConfirmRequestDto(), OWNER_ID);

        assertThat(options).filteredOn(option -> Boolean.TRUE.equals(option.getConfirmed()))
                .extracting(VoteOptionEntity::getOptionId)
                .containsExactly(1L, 2L, 3L, 4L, 5L, 6L);
        assertThat(vote.getConfirmedOptionId()).isEqualTo(1L);
    }

    @Test
    void 숙소는_여행_길이와_무관하게_한_곳만_확정한다() {
        vote.setCategory(TourPlaceCategory.ACCOMMODATION);
        List<VoteOptionEntity> options = options(3);
        given(voteOptionRepository.findByVoteIdOrderByCreatedAtAsc(VOTE_ID))
                .willReturn(options);
        given(voteRecordRepository.countByOption(VOTE_ID))
                .willReturn(List.of(row(1, 3), row(2, 2), row(3, 1)));

        service.confirmVote(PLANNER_ID, VOTE_ID, new VoteConfirmRequestDto(), OWNER_ID);

        assertThat(options).filteredOn(option -> Boolean.TRUE.equals(option.getConfirmed()))
                .extracting(VoteOptionEntity::getOptionId)
                .containsExactly(1L);
    }

    @Test
    void 삼일_명소는_득표순_세_곳을_확정한다() {
        vote.setCategory(TourPlaceCategory.ATTRACTION);
        List<VoteOptionEntity> options = options(4);
        given(voteOptionRepository.findByVoteIdOrderByCreatedAtAsc(VOTE_ID))
                .willReturn(options);
        given(voteRecordRepository.countByOption(VOTE_ID))
                .willReturn(List.of(row(1, 4), row(2, 3), row(3, 2), row(4, 1)));

        service.confirmVote(PLANNER_ID, VOTE_ID, new VoteConfirmRequestDto(), OWNER_ID);

        assertThat(options).filteredOn(option -> Boolean.TRUE.equals(option.getConfirmed()))
                .extracting(VoteOptionEntity::getOptionId)
                .containsExactly(1L, 2L, 3L);
    }

    @Test
    void 마지막_자리_동점은_선택한_후보를_포함한다() {
        // 8번은 경계 동점(1표)보다 낮은 0표 후보이다. 이 후보가 보장 후보로
        // 잘못 수집되면 boundarySeatsBeforeLast 가 음수가 된다.
        List<VoteOptionEntity> options = options(8);
        given(voteOptionRepository.findByVoteIdOrderByCreatedAtAsc(VOTE_ID))
                .willReturn(options);
        given(voteRecordRepository.countByOption(VOTE_ID)).willReturn(List.of(
                row(1, 7), row(2, 6), row(3, 5), row(4, 4),
                row(5, 3), row(6, 1), row(7, 1)));

        VoteConfirmRequestDto request = new VoteConfirmRequestDto();
        request.setOptionId(7L);
        service.confirmVote(PLANNER_ID, VOTE_ID, request, OWNER_ID);

        assertThat(options).filteredOn(option -> Boolean.TRUE.equals(option.getConfirmed()))
                .extracting(VoteOptionEntity::getOptionId)
                .containsExactlyInAnyOrder(1L, 2L, 3L, 4L, 5L, 7L);
    }

    @Test
    void 마지막_자리_동점인데_선택하지_않으면_거부한다() {
        List<VoteOptionEntity> options = options(7);
        given(voteOptionRepository.findByVoteIdOrderByCreatedAtAsc(VOTE_ID))
                .willReturn(options);
        given(voteRecordRepository.countByOption(VOTE_ID)).willReturn(List.of(
                row(1, 7), row(2, 6), row(3, 5), row(4, 4),
                row(5, 3), row(6, 1), row(7, 1)));

        assertThatThrownBy(() -> service.confirmVote(
                PLANNER_ID, VOTE_ID, new VoteConfirmRequestDto(), OWNER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("마지막 자리");
    }

    @Test
    void 영표_장바구니는_직접_고른_한_곳만_확정한다() {
        List<VoteOptionEntity> options = options(4);
        given(voteOptionRepository.findByVoteIdOrderByCreatedAtAsc(VOTE_ID))
                .willReturn(options);
        given(voteRecordRepository.countByOption(VOTE_ID)).willReturn(List.of());

        VoteConfirmRequestDto request = new VoteConfirmRequestDto();
        request.setOptionId(3L);
        service.confirmVote(PLANNER_ID, VOTE_ID, request, OWNER_ID);

        assertThat(options).filteredOn(option -> Boolean.TRUE.equals(option.getConfirmed()))
                .extracting(VoteOptionEntity::getOptionId)
                .containsExactly(3L);
    }

    private List<VoteOptionEntity> options(int count) {
        return IntStream.rangeClosed(1, count).mapToObj(index -> {
            VoteOptionEntity option = new VoteOptionEntity();
            option.setOptionId((long) index);
            option.setVoteId(VOTE_ID);
            option.setPlaceName("장소 " + index);
            option.setCreatedAt(LocalDateTime.of(2026, 8, 1, 0, 0).plusSeconds(index));
            return option;
        }).toList();
    }

    private Object[] row(long optionId, long count) {
        return new Object[]{optionId, count};
    }
}
