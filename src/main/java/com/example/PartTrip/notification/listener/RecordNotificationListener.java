package com.example.PartTrip.notification.listener;

import com.example.PartTrip.notification.enums.NotificationType;
import com.example.PartTrip.notification.repository.NotificationRepository;
import com.example.PartTrip.notification.event.RegionVisitedEvent;
import com.example.PartTrip.notification.event.TripCardCreatedEvent;
import com.example.PartTrip.region.enums.RegionCode;
import com.example.PartTrip.notification.service.NotificationWriter;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final NotificationRepository notificationRepository;
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

        // 시·도 코드를 링크 대상으로 그대로 쓴다. 17개 모두 앞자리 0 이 없는 두 자리다
        Long regionId = Long.valueOf(RegionCode.of(event.regionCode()).getCode());

        try {
            // 이 지역을 이미 알렸으면 두 번 보내지 않는다.
            // 여행카드를 세지 않고 여기서 보는 이유는, 카드를 세는 쪽은 아직 커밋
            // 전이라 같은 순간에 확정된 다른 카드가 보이지 않기 때문이다
            if (notificationRepository.existsByUserIdAndTypeAndLinkId(
                    event.actorUserId(), NotificationType.REGION_VISITED, regionId)) {
                return;
            }

            notificationWriter.write(
                    event.actorUserId(),
                    NotificationType.REGION_VISITED,
                    NotificationType.REGION_VISITED.getLabel(),
                    RegionCode.nameOf(event.regionCode()) + " 방문이 지도에 기록됐어요.",
                    "REGION_MAP",
                    regionId);

        } catch (DataIntegrityViolationException alreadySent) {
            // 위 검사를 둘이 동시에 통과했다. 유니크 인덱스가 늦은 쪽을 막은 것이라
            // 알림은 이미 하나 나갔다 (db/domestic_region_map.sql)
            log.debug("새 지역 방문 알림 중복 regionCode={}", event.regionCode());
        } catch (Exception e) {
            log.warn("새 지역 방문 알림 실패 regionCode={}", event.regionCode(), e);
        }
    }
}
