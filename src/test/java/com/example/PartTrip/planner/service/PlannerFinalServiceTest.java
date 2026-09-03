package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.enums.VoteStatus;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteOptionRepository;
import com.example.PartTrip.planner.repository.VoteRecordRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PlannerFinalServiceTest {

    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private VoteRepository voteRepository;
    @Mock private VoteOptionRepository voteOptionRepository;
    @Mock private VoteRecordRepository voteRecordRepository;
    @Mock private TourPlaceRepository tourPlaceRepository;
    @InjectMocks private PlannerFinalService service;

    @Test
    void 삭제된_레거시_확정_후보가_있으면_최종_일정을_거부한다() {
        long plannerId = 1L;
        long planId = 10L;
        long missingOptionId = 999L;
        String userId = "member";

        TravelGroupEntity group = new TravelGroupEntity();
        group.setGroupId(plannerId);
        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setPlanId(planId);
        VoteEntity vote = new VoteEntity();
        vote.setVoteId(100L);
        vote.setPlanId(planId);
        vote.setCategory(TourPlaceCategory.RESTAURANT);
        vote.setStatus(VoteStatus.CONFIRMED);
        vote.setConfirmedOptionId(missingOptionId);

        given(travelGroupRepository.findById(plannerId)).willReturn(Optional.of(group));
        given(groupMemberRepository.existsByGroupIdAndUserId(plannerId, userId)).willReturn(true);
        given(groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(plannerId))
                .willReturn(Optional.of(plan));
        given(voteRepository.findByPlanId(planId)).willReturn(List.of(vote));
        given(voteOptionRepository.findByVoteIdInAndConfirmedTrue(List.of(vote.getVoteId())))
                .willReturn(List.of());
        given(voteOptionRepository.findAllById(List.of(missingOptionId))).willReturn(List.of());

        assertThatThrownBy(() -> service.getConfirmedPlaces(plannerId, userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("확정된 투표 후보 정보를 찾을 수 없습니다.");
    }
}
