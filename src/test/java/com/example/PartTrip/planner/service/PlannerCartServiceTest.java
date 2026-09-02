package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.request.PlannerCartRequestDto;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.TravelGroupRepository;
import com.example.PartTrip.planner.repository.VoteOptionRepository;
import com.example.PartTrip.planner.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlannerCartServiceTest {

    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private VoteRepository voteRepository;
    @Mock private VoteOptionRepository voteOptionRepository;
    @Mock private TourPlaceRepository tourPlaceRepository;
    @InjectMocks private PlannerCartService plannerCartService;

    @Test
    void 장소와_투표를_읽기_전에_플래너를_잠근다() {
        when(travelGroupRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(new TravelGroupEntity()));
        when(groupMemberRepository.existsByGroupIdAndUserId(1L, "outsider"))
                .thenReturn(false);

        assertThatThrownBy(() -> plannerCartService.addPlaces(
                1L, new PlannerCartRequestDto(), "outsider"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("멤버");

        verify(travelGroupRepository).findByIdForUpdate(1L);
    }
}
