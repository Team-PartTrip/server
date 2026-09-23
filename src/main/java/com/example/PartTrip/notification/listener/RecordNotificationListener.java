package com.example.PartTrip.notification.listener;

import com.example.PartTrip.notification.enums.NotificationType;
import com.example.PartTrip.notification.event.RegionVisitedEvent;
import com.example.PartTrip.notification.event.TripCardCreatedEvent;
import com.example.PartTrip.region.enums.RegionCode;
import com.example.PartTrip.notification.service.NotificationWriter;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

// 여행 카드 · 기록 · 대한민국 지도에서 올라온 이벤트를 알림으로 바꾼다
// (NotificationCategory.RECORD)
//
// 세 가지 모두 행동한 본인이 받는 알림이다. 예외 처리 방침은 PlannerNotificationListener 와 같다.
@Slf4j
@Component
@RequiredArgsConstructor
public class RecordNotificationListener {

    private final NotificationWriter notificationWriter;
    private final TripCardRepository tripCardRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(TripCardCreatedEvent event) {

        try {
            String cardTitle = tripCardRepository.findById(event.tripCardId())
                    .map(TripCardEntity::getTitle)
                    .orElse("여행");

            notificationWriter.write(
                    event.actorUserId(),
                    NotificationType.TRIP_CARD_CREATED,
                    NotificationType.TRIP_CARD_CREATED.getLabel(),
                    cardTitle + " 여행카드가 만들어졌어요.",
                    "TRIP_CARD",
                    event.tripCardId());

        } catch (Exception e) {
            log.warn("여행카드 생성 알림 실패 tripCardId={}", event.tripCardId(), e);
        }
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(RegionVisitedEvent event) {

        try {
            notificationWriter.write(
                    event.actorUserId(),
                    NotificationType.REGION_VISITED,
                    NotificationType.REGION_VISITED.getLabel(),
                    RegionCode.nameOf(event.regionCode()) + " 방문이 지도에 기록됐어요.",
                    "REGION_MAP",
                    null);

        } catch (Exception e) {
            log.warn("새 지역 방문 알림 실패 regionCode={}", event.regionCode(), e);
        }
    }
}
