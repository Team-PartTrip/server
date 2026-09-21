package com.example.PartTrip.planner.service;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.response.ConfirmedPlaceResponseDto;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.TravelGroupEntity;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

/** 확정 일정은 여행 카드에 저장된 날짜 · 순서 그대로 돌려준다 (#161) */
@ExtendWith(MockitoExtension.class)
class PlannerFinalServiceTest {

    private static final long PLANNER_ID = 1L;
    private static final long PLAN_ID = 10L;
    private static final String USER_ID = "member";
    private static final LocalDate DAY1 = LocalDate.of(2026, 10, 10);
    private static final LocalDate DAY2 = LocalDate.of(2026, 10, 11);

    @Mock private TravelGroupRepository travelGroupRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private TripCardRepository tripCardRepository;
    @Mock private TripCardPlaceRepository tripCardPlaceRepository;
    @Mock private TourPlaceRepository tourPlaceRepository;
    @InjectMocks private PlannerFinalService service;

    @BeforeEach
    void setUp() {
        TravelGroupEntity group = new TravelGroupEntity();
        group.setGroupId(PLANNER_ID);
        group.setStatus(GroupStatus.CONFIRMED);
        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setPlanId(PLAN_ID);

        given(travelGroupRepository.findById(PLANNER_ID)).willReturn(Optional.of(group));
        given(groupMemberRepository.existsByGroupIdAndUserId(PLANNER_ID, USER_ID)).willReturn(true);
        given(groupTravelPlanRepository.findFirstByGroupIdOrderByCreatedAtDesc(PLANNER_ID))
                .willReturn(Optional.of(plan));
    }

    private TripCardEntity card(long id, String userId) {
        return TripCardEntity.builder().tripCardId(id).userId(userId).planId(PLAN_ID).build();
    }

    private TripCardPlaceEntity cardPlace(long tourPlaceId, LocalDate date) {
        TripCardPlaceEntity place = new TripCardPlaceEntity();
        place.setTourPlaceId(tourPlaceId);
        place.setPlaceName("장소 " + tourPlaceId);
        place.setVisitedDate(date);
        return place;
    }

    @Test
    void 날짜와_카테고리를_붙여_돌려준다() {
        given(tripCardRepository.findByPlanIdIn(List.of(PLAN_ID)))
                .willReturn(List.of(card(7L, USER_ID)));
        given(tripCardPlaceRepository.findByTripCardIdOrderByVisitedDateAscSortOrderAsc(7L))
                .willReturn(List.of(cardPlace(1L, DAY1), cardPlace(2L, DAY2)));
        TourPlaceEntity restaurant = new TourPlaceEntity();
        restaurant.setTourPlaceId(1L);
        restaurant.setCategory(TourPlaceCategory.RESTAURANT);
        given(tourPlaceRepository.findAllById(Set.of(1L, 2L))).willReturn(List.of(restaurant));

        List<ConfirmedPlaceResponseDto> places = service.getConfirmedPlaces(PLANNER_ID, USER_ID)
                .getPlaces();

        // 2번은 관광지 데이터가 지워졌다. 카드에 이름이 남아 있으니 빼지 않는다
        assertThat(places)
                .extracting(ConfirmedPlaceResponseDto::getTourPlaceId,
                        ConfirmedPlaceResponseDto::getVisitedDate,
                        ConfirmedPlaceResponseDto::getCategoryLabel)
                .containsExactly(
                        Tuple.tuple(1L, DAY1, "맛집"),
                        Tuple.tuple(2L, DAY2, null));
    }

    @Test
    void 내_카드를_지웠으면_다른_멤버의_카드를_읽는다() {
        given(tripCardRepository.findByPlanIdIn(List.of(PLAN_ID)))
                .willReturn(List.of(card(8L, "owner")));
        given(tripCardPlaceRepository.findByTripCardIdOrderByVisitedDateAscSortOrderAsc(8L))
                .willReturn(List.of(cardPlace(1L, DAY1)));

        assertThat(service.getConfirmedPlaces(PLANNER_ID, USER_ID).getPlaces()).hasSize(1);
    }

    @Test
    void 확정_전이면_거부한다() {
        given(tripCardRepository.findByPlanIdIn(List.of(PLAN_ID))).willReturn(List.of());

        assertThatThrownBy(() -> service.getConfirmedPlaces(PLANNER_ID, USER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아직 일정이 확정되지 않았습니다.");
    }
}
