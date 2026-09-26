package com.example.PartTrip.planner.service;

import com.example.PartTrip.global.exception.ForbiddenException;
import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.planner.dto.request.SavePlannerScheduleRequestDto;
import com.example.PartTrip.planner.entity.*;
import com.example.PartTrip.planner.enums.GroupStatus;
import com.example.PartTrip.planner.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlannerScheduleEditServiceTest {
    @Mock TravelGroupRepository groups;
    @Mock GroupMemberRepository members;
    @Mock GroupTravelPlanRepository plans;
    @Mock PlannerCityRepository cities;
    @Mock PlannerScheduleSlotRepository slots;
    @Mock TourPlaceRepository places;
    @Mock PlannerDraftService drafts;
    @InjectMocks PlannerScheduleEditService service;
    final LocalDate date = LocalDate.of(2026, 10, 10);

    private void owner() {
        var group = new TravelGroupEntity();
        group.setOwnerUserId("owner");
        group.setStatus(GroupStatus.PLANNING);
        when(groups.findByIdForUpdate(1L)).thenReturn(Optional.of(group));
    }

    private GroupTravelPlanEntity plan() {
        var plan = new GroupTravelPlanEntity();
        plan.setPlanId(10L);
        plan.setRegionCode("51");
        plan.setCityName("강릉시");
        plan.setStartDate(date);
        plan.setEndDate(date.plusDays(1));
        when(plans.findFirstByGroupIdOrderByCreatedAtDesc(1L)).thenReturn(Optional.of(plan));
        return plan;
    }

    private SavePlannerScheduleRequestDto request(SavePlannerScheduleRequestDto.Slot... values) {
        return new SavePlannerScheduleRequestDto(List.of(new SavePlannerScheduleRequestDto.Day(date, List.of(values))));
    }

    /** 멤버의 쓰기 요청은 저장 전에 거부한다. */
    @Test void rejectsMember() {
        owner();
        assertThatThrownBy(() -> service.save(1L, request(), "member")).isInstanceOf(ForbiddenException.class);
        verifyNoInteractions(slots);
    }

    /** 카드 식별자를 유지한 채 순서를 바꾸고 빈 카드를 추가한다. */
    @Test void preservesOrderAndEmptySlot() {
        owner();
        plan();
        var first = new PlannerScheduleSlotEntity(10L, date, 1, null);
        first.setSlotId(11L);
        var second = new PlannerScheduleSlotEntity(10L, date, 2, null);
        second.setSlotId(12L);
        when(slots.findByPlanIdOrderByVisitDateAscSortOrderAsc(10L)).thenReturn(List.of(first, second));
        service.save(1L, request(new SavePlannerScheduleRequestDto.Slot(12L, null),
                new SavePlannerScheduleRequestDto.Slot(11L, null), new SavePlannerScheduleRequestDto.Slot(null, null)), "owner");
        assertThat(second.getSortOrder()).isEqualTo(1);
        assertThat(first.getSortOrder()).isEqualTo(2);
        verify(slots).saveAllAndFlush(argThat(rows -> {
            var list = new ArrayList<PlannerScheduleSlotEntity>();
            rows.forEach(list::add);
            return list.size() == 3 && list.get(2).getTourPlaceId() == null && list.get(2).getSortOrder() == 3;
        }));
    }

    /** 다른 플래너의 슬롯을 가져와 덮어쓸 수 없다. */
    @Test void rejectsForeignSlot() {
        owner(); plan();
        assertThatThrownBy(() -> service.save(1L, request(new SavePlannerScheduleRequestDto.Slot(99L, null)), "owner"))
                .isInstanceOf(IllegalArgumentException.class);
        verify(slots, never()).saveAllAndFlush(any());
    }

    /** 전체 삭제를 기록해 확정 시 추천 장소가 되살아나지 않도록 한다. */
    @Test void remembersEmptySchedule() {
        owner();
        var plan = plan();
        service.save(1L, request(), "owner");
        assertThat(plan.getScheduleEdited()).isTrue();
    }

    /** 오늘 선택한 곳은 제외하되 다른 날 선택한 곳은 검색할 수 있다. */
    @Test void excludesOnlySameDayPlaces() {
        plan();
        when(members.existsByGroupIdAndUserId(1L, "owner")).thenReturn(true);
        when(slots.findByPlanIdOrderByVisitDateAscSortOrderAsc(10L)).thenReturn(List.of(
                new PlannerScheduleSlotEntity(10L, date, 1, 1L),
                new PlannerScheduleSlotEntity(10L, date.plusDays(1), 1, 2L)));
        var first = new TourPlaceEntity(); first.setTourPlaceId(1L); first.setPlaceName("카페 하나");
        var second = new TourPlaceEntity(); second.setTourPlaceId(2L); second.setPlaceName("카페 둘");
        when(places.search("대한민국", "강릉시", null)).thenReturn(List.of(first, second));
        assertThat(service.candidates(1L, date, "카페", "owner"))
                .extracting(p -> p.tourPlaceId()).containsExactly(2L);
    }
}
