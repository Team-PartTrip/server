package com.example.PartTrip.notification.scheduler;

import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import com.example.PartTrip.notification.enums.NotificationType;
import com.example.PartTrip.notification.repository.NotificationRepository;
import com.example.PartTrip.notification.service.NotificationWriter;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.entity.PlannerScheduleSlotEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.planner.repository.PlannerScheduleSlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationSchedulerTest {

    @Mock private NotificationWriter notificationWriter;
    @Mock private NotificationRepository notificationRepository;
    @Mock private GroupTravelPlanRepository planRepository;
    @Mock private GroupMemberRepository groupMemberRepository;
    @Mock private PlannerScheduleSlotRepository slotRepository;
    @Mock private TourPlaceRepository tourPlaceRepository;

    @InjectMocks private NotificationScheduler scheduler;

    @Test
    void 내일_떠나는_그룹은_전원이_받는다() {

        given(planRepository.findConfirmedStartingOn(any())).willReturn(List.of(plan()));
        given(groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(7L))
                .willReturn(List.of(member("할머니"), member("손주")));
        given(notificationRepository.existsByUserIdAndTypeAndLinkId(
                anyString(), eq(NotificationType.TRIP_DAY_BEFORE), eq(7L))).willReturn(false);

        scheduler.notifyTripDayBefore();

        verify(notificationWriter).writeAll(
                eq(List.of("할머니", "손주")),
                eq(NotificationType.TRIP_DAY_BEFORE),
                anyString(),
                eq("내일 오사카 여행 출발이에요."),
                eq("PLANNER"),
                eq(7L));
    }

    @Test
    void 이미_받은_사람에게는_다시_보내지_않는다() {

        given(planRepository.findConfirmedStartingOn(any())).willReturn(List.of(plan()));
        given(groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(7L))
                .willReturn(List.of(member("할머니"), member("손주")));
        given(notificationRepository.existsByUserIdAndTypeAndLinkId(
                "할머니", NotificationType.TRIP_DAY_BEFORE, 7L)).willReturn(true);
        given(notificationRepository.existsByUserIdAndTypeAndLinkId(
                "손주", NotificationType.TRIP_DAY_BEFORE, 7L)).willReturn(false);

        scheduler.notifyTripDayBefore();

        verify(notificationWriter).writeAll(
                eq(List.of("손주")), any(), anyString(), anyString(), anyString(), any());
    }

    @Test
    void 전원이_이미_받았으면_아무것도_보내지_않는다() {

        given(planRepository.findConfirmedStartingOn(any())).willReturn(List.of(plan()));
        given(groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(7L))
                .willReturn(List.of(member("할머니")));
        given(notificationRepository.existsByUserIdAndTypeAndLinkId(
                anyString(), any(), any())).willReturn(true);

        scheduler.notifyTripDayBefore();

        verify(notificationWriter, never()).writeAll(any(), any(), any(), any(), any(), any());
    }

    @Test
    void 오늘_가는_곳을_순서대로_적어_보낸다() {

        given(planRepository.findConfirmedCovering(any())).willReturn(List.of(plan()));
        given(slotRepository.findByPlanIdAndVisitDateOrderBySortOrderAsc(any(), any()))
                .willReturn(List.of(slot(1, 100L), slot(2, 200L)));
        given(tourPlaceRepository.findAllById(any()))
                .willReturn(List.of(place(100L, "도톤보리"), place(200L, "오사카성")));
        given(groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(7L))
                .willReturn(List.of(member("할머니")));
        given(notificationRepository.existsByUserIdAndTypeAndLinkIdAndCreatedAtAfter(
                anyString(), eq(NotificationType.TODAY_SCHEDULE), eq(7L), any())).willReturn(false);

        scheduler.notifyTodaySchedule();

        verify(notificationWriter).writeAll(
                eq(List.of("할머니")),
                eq(NotificationType.TODAY_SCHEDULE),
                anyString(),
                eq("오늘은 도톤보리 · 오사카성 일정이에요."),
                eq("PLANNER"),
                eq(7L));
    }

    // 오늘 일정은 같은 플래너로 매일 나가므로 linkId 만으로는 중복을 가릴 수 없다
    @Test
    void 오늘_이미_보냈으면_다시_보내지_않는다() {

        given(planRepository.findConfirmedCovering(any())).willReturn(List.of(plan()));
        given(slotRepository.findByPlanIdAndVisitDateOrderBySortOrderAsc(any(), any()))
                .willReturn(List.of(slot(1, 100L)));
        given(tourPlaceRepository.findAllById(any()))
                .willReturn(List.of(place(100L, "도톤보리")));
        given(groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(7L))
                .willReturn(List.of(member("할머니")));
        given(notificationRepository.existsByUserIdAndTypeAndLinkIdAndCreatedAtAfter(
                anyString(), any(), any(), any())).willReturn(true);

        scheduler.notifyTodaySchedule();

        verify(notificationWriter, never()).writeAll(any(), any(), any(), any(), any(), any());
    }

    // 일정을 아직 안 짠 플래너에 빈 알림을 보내면 열어 보고 허탕만 친다
    @Test
    void 오늘_갈_곳이_없으면_오늘_일정을_보내지_않는다() {

        given(planRepository.findConfirmedCovering(any()))
                .willReturn(List.of(plan()));
        given(slotRepository.findByPlanIdAndVisitDateOrderBySortOrderAsc(any(), any()))
                .willReturn(List.of());

        scheduler.notifyTodaySchedule();

        verify(notificationWriter, never()).writeAll(any(), any(), any(), any(), any(), any());
    }

    // 한 플래너가 실패해도 나머지는 받아야 한다. 예전 CHECK 제약에 막히는 경우가 여기다(#195)
    @Test
    void 한_플래너가_실패해도_다음_플래너는_보낸다() {

        GroupTravelPlanEntity broken = plan();
        broken.setGroupId(99L);

        given(planRepository.findConfirmedStartingOn(any())).willReturn(List.of(broken, plan()));
        given(groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(99L))
                .willThrow(new RuntimeException("조회 실패"));
        given(groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(7L))
                .willReturn(List.of(member("손주")));
        given(notificationRepository.existsByUserIdAndTypeAndLinkId(
                anyString(), any(), any())).willReturn(false);

        scheduler.notifyTripDayBefore();

        verify(notificationWriter).writeAll(
                eq(List.of("손주")), any(), anyString(), anyString(), anyString(), eq(7L));
    }

    @Test
    void 장소가_셋_이하면_그대로_적는다() {

        assertThat(NotificationScheduler.joinPlaces(List.of("도톤보리", "구로몬시장")))
                .isEqualTo("도톤보리 · 구로몬시장");
    }

    @Test
    void 장소가_넷_이상이면_앞의_셋만_적고_나머지는_수로_줄인다() {

        assertThat(NotificationScheduler.joinPlaces(
                List.of("도톤보리", "구로몬시장", "오사카성", "우메다", "신사이바시")))
                .isEqualTo("도톤보리 · 구로몬시장 · 오사카성 외 2곳");
    }

    @Test
    void 갈_곳이_없으면_문구가_없다() {

        assertThat(NotificationScheduler.joinPlaces(List.of())).isNull();
    }

    private GroupTravelPlanEntity plan() {

        GroupTravelPlanEntity plan = new GroupTravelPlanEntity();
        plan.setPlanId(10L);
        plan.setGroupId(7L);
        plan.setTravelTitle("오사카 여행");
        plan.setCityName("오사카");
        plan.setStartDate(LocalDate.now().plusDays(1));
        plan.setEndDate(LocalDate.now().plusDays(3));
        return plan;
    }

    private PlannerScheduleSlotEntity slot(int sortOrder, Long tourPlaceId) {

        return new PlannerScheduleSlotEntity(10L, LocalDate.now(), sortOrder, tourPlaceId);
    }

    private TourPlaceEntity place(Long id, String placeName) {

        TourPlaceEntity place = new TourPlaceEntity();
        place.setTourPlaceId(id);
        place.setPlaceName(placeName);
        return place;
    }

    private GroupMemberEntity member(String userId) {

        GroupMemberEntity member = new GroupMemberEntity();
        member.setGroupId(7L);
        member.setUserId(userId);
        return member;
    }
}
