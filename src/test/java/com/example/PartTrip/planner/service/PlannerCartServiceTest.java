package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.request.PlannerCartRequestDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
import com.example.PartTrip.planner.entity.VoteEntity;
import com.example.PartTrip.planner.enums.GroupStatus;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
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

    @Test
    void 그룹장이_한번에_여행지의_전체_장소로_투표를_시작한다() {
        TravelGroupEntity group = new TravelGroupEntity();
        group.setOwnerUserId("owner");
        group.setStatus(GroupStatus.PLANNING);
        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setPlanId(10L);
        plan.setCountryName("일본");
        plan.setCityName("오사카");
        TourPlaceEntity attraction = place(1L, "오사카성", TourPlaceCategory.ATTRACTION);
        TourPlaceEntity restaurant = place(2L, "타코야키 맛집", TourPlaceCategory.RESTAURANT);

        when(travelGroupRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(group));
        when(groupMemberRepository.existsByGroupIdAndUserId(1L, "owner")).thenReturn(true);
        when(groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.of(plan));
        when(tourPlaceRepository.findByCountryNameAndCityName("일본", "오사카"))
                .thenReturn(List.of(attraction, restaurant));
        when(voteRepository.findByPlanId(10L)).thenReturn(List.of());
        when(voteRepository.saveAll(anyList())).thenAnswer(invocation -> {
            List<VoteEntity> votes = invocation.getArgument(0);
            long voteId = 100L;
            for (VoteEntity vote : votes) {
                vote.setVoteId(voteId++);
            }
            return votes;
        });
        when(voteOptionRepository.findByVoteIdInOrderByCreatedAtAsc(anyList()))
                .thenReturn(List.of());

        String result = plannerCartService.startVoting(1L, "owner");

        assertThat(result).isEqualTo("2개 장소를 투표 후보로 등록했습니다.");
        verify(tourPlaceRepository).findByCountryNameAndCityName("일본", "오사카");
        verify(voteOptionRepository).saveAll(anyList());
    }

    @Test
    void 그룹원이_전체_장소_투표를_시작할_수_없다() {
        TravelGroupEntity group = new TravelGroupEntity();
        group.setOwnerUserId("owner");
        group.setStatus(GroupStatus.PLANNING);
        when(travelGroupRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(group));
        when(groupMemberRepository.existsByGroupIdAndUserId(1L, "member")).thenReturn(true);

        assertThatThrownBy(() -> plannerCartService.startVoting(1L, "member"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("그룹장");
    }

    @Test
    void 확정된_플래너에서는_새_투표를_시작할_수_없다() {
        TravelGroupEntity group = new TravelGroupEntity();
        group.setOwnerUserId("owner");
        group.setStatus(GroupStatus.CONFIRMED);
        when(travelGroupRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(group));
        when(groupMemberRepository.existsByGroupIdAndUserId(1L, "owner")).thenReturn(true);

        assertThatThrownBy(() -> plannerCartService.startVoting(1L, "owner"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("현재 상태");
    }

    private TourPlaceEntity place(Long id, String name, TourPlaceCategory category) {
        TourPlaceEntity place = new TourPlaceEntity();
        place.setTourPlaceId(id);
        place.setPlaceName(name);
        place.setCategory(category);
        return place;
    }
}
