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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// 날짜가 되어 나가는 알림 (#143 · Func-004-01)
//
// 다른 알림은 누가 무엇을 해서 생기므로 이벤트 → 리스너로 만든다. 이 둘은
// 아무도 아무것도 하지 않아도 날짜가 되면 생겨야 해서 해 줄 사람이 없다.
// 그래서 TripCardScheduler 처럼 스케줄러가 맡는다.
//
// 보내는 시각을 프로퍼티로 뺀 것은 시연에서 짧게 돌리기 위해서다.
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationWriter notificationWriter;
    private final NotificationRepository notificationRepository;
    private final GroupTravelPlanRepository planRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final PlannerScheduleSlotRepository slotRepository;
    private final TourPlaceRepository tourPlaceRepository;

    // 문구가 길어지면 목록이 읽기 어렵다. 앞의 몇 곳만 적고 나머지는 수로 줄인다
    private static final int PLACES_IN_BODY = 3;

    /** 내일 떠나는 사람에게 (Func-004-01 "여행 하루 전") */
    @Scheduled(cron = "${part-trip.notification.trip-day-before-cron:0 0 9 * * *}")
    public void notifyTripDayBefore() {

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        for (GroupTravelPlanEntity plan : planRepository.findByStartDate(tomorrow)) {
            try {
                // 플래너 하나당 한 번이라 linkId 만으로 중복이 가려진다
                List<String> recipients = recipientsOf(
                        plan, NotificationType.TRIP_DAY_BEFORE, null);
                if (recipients.isEmpty()) {
                    continue;
                }

                notificationWriter.writeAll(
                        recipients,
                        NotificationType.TRIP_DAY_BEFORE,
                        NotificationType.TRIP_DAY_BEFORE.getLabel(),
                        "내일 " + titleOf(plan) + " 출발이에요.",
                        "PLANNER",
                        plan.getGroupId());

            } catch (Exception e) {
                // 한 플래너가 실패해도 나머지는 보낸다.
                // 저장이 막히는 원인 중 하나가 예전 CHECK 제약이라(#195, db/notification_three_types.sql)
                // 묻히지 않도록 error 로 남긴다
                log.error("여행 하루 전 알림 실패 planId={}", plan.getPlanId(), e);
            }
        }
    }

    /** 오늘 무엇을 하는지 (Func-004-01 "오늘 일정") */
    @Scheduled(cron = "${part-trip.notification.today-schedule-cron:0 0 8 * * *}")
    public void notifyTodaySchedule() {

        LocalDate today = LocalDate.now();

        for (GroupTravelPlanEntity plan :
                planRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today)) {
            try {
                String places = placesOn(plan.getPlanId(), today);
                // 일정을 아직 안 짰거나 오늘 갈 곳이 없으면 보낼 내용이 없다.
                // 빈 알림을 보내면 열어 보고 허탕만 친다
                if (places == null) {
                    continue;
                }

                // 같은 플래너로 매일 나가므로 "오늘 이미 보냈는지" 로 가린다
                List<String> recipients = recipientsOf(
                        plan, NotificationType.TODAY_SCHEDULE, today);
                if (recipients.isEmpty()) {
                    continue;
                }

                notificationWriter.writeAll(
                        recipients,
                        NotificationType.TODAY_SCHEDULE,
                        NotificationType.TODAY_SCHEDULE.getLabel(),
                        "오늘은 " + places + " 일정이에요.",
                        "PLANNER",
                        plan.getGroupId());

            } catch (Exception e) {
                log.error("오늘 일정 알림 실패 planId={}", plan.getPlanId(), e);
            }
        }
    }

    /**
     * 이 플래너에서 아직 알림을 못 받은 멤버.
     *
     * <p>{@code sentAfter} 가 있으면 그 시각 이후에 받았는지로 본다(매일 나가는 알림),
     * 없으면 한 번이라도 받았는지로 본다(플래너당 한 번인 알림).
     *
     * <p>한 명씩 확인하는 것은 플래너 수도 인원도 작기 때문이다. 같은 날 떠나는
     * 플래너가 수천 개가 되면 그때 한 번에 읽는 쿼리로 바꾼다
     */
    private List<String> recipientsOf(
            GroupTravelPlanEntity plan, NotificationType type, LocalDate sentAfter) {

        return groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(plan.getGroupId()).stream()
                .map(GroupMemberEntity::getUserId)
                .filter(userId -> sentAfter == null
                        ? !notificationRepository.existsByUserIdAndTypeAndLinkId(
                                userId, type, plan.getGroupId())
                        : !notificationRepository.existsByUserIdAndTypeAndLinkIdAndCreatedAtAfter(
                                userId, type, plan.getGroupId(), sentAfter.atStartOfDay()))
                .toList();
    }

    /** 그날 가는 곳을 문구로. 갈 곳이 없으면 null */
    private String placesOn(Long planId, LocalDate date) {

        List<PlannerScheduleSlotEntity> slots =
                slotRepository.findByPlanIdAndVisitDateOrderBySortOrderAsc(planId, date);

        Map<Long, TourPlaceEntity> placesById = tourPlaceRepository.findAllById(slots.stream()
                        .map(PlannerScheduleSlotEntity::getTourPlaceId)
                        .filter(id -> id != null)
                        .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(TourPlaceEntity::getTourPlaceId, Function.identity()));

        // 슬롯 순서대로 이름을 뽑는다. 장소가 지워졌으면 그 자리는 건너뛴다
        List<String> names = slots.stream()
                .map(slot -> placesById.get(slot.getTourPlaceId()))
                .filter(place -> place != null)
                .map(TourPlaceEntity::getPlaceName)
                .toList();

        return joinPlaces(names);
    }

    /**
     * 앞의 몇 곳만 적고 나머지는 수로 줄인다. 갈 곳이 없으면 null.
     *
     * <p>리포지토리 없이 바로 시험할 수 있도록 떼어 뒀다
     */
    static String joinPlaces(List<String> names) {

        if (names.isEmpty()) {
            return null;
        }

        if (names.size() <= PLACES_IN_BODY) {
            return String.join(" · ", names);
        }

        return String.join(" · ", names.subList(0, PLACES_IN_BODY))
                + " 외 " + (names.size() - PLACES_IN_BODY) + "곳";
    }

    private String titleOf(GroupTravelPlanEntity plan) {

        return plan.getTravelTitle() == null ? plan.getCityName() : plan.getTravelTitle();
    }
}
