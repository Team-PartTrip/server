package com.example.PartTrip.notification.listener;

import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import com.example.PartTrip.notification.enums.NotificationType;
import com.example.PartTrip.notification.event.CountryAcquiredEvent;
import com.example.PartTrip.notification.event.PhotoOrganizedEvent;
import com.example.PartTrip.notification.event.TripCardCreatedEvent;
import com.example.PartTrip.notification.service.NotificationWriter;
import com.example.PartTrip.photo.repository.PhotoAnalysisRepository;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

// 여행 카드 · 기록 · 세계지도에서 올라온 이벤트를 알림으로 바꾼다
// (NotificationCategory.RECORD)
//
// 세 가지 모두 행동한 본인이 받는 알림이다. 예외 처리 방침은 PlannerNotificationListener 와 같다.
@Slf4j
@Component
@RequiredArgsConstructor
public class RecordNotificationListener {

    private final NotificationWriter notificationWriter;
    private final TripCardRepository tripCardRepository;
    private final CountryInfoRepository countryInfoRepository;
    private final PhotoAnalysisRepository photoAnalysisRepository;

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
    public void on(PhotoOrganizedEvent event) {

        try {
            // 분석이 끝나야 알릴 내용이 생긴다. 제목이 있으면 무엇을 찍었는지 함께 보여준다.
            String subject = photoAnalysisRepository.findByPhotoPhotoId(event.photoId())
                    .map(analysis -> analysis.getTitle())
                    .orElse(null);

            String body = subject == null
                    ? "사진 분석이 끝났어요. 기록에서 확인해보세요."
                    : subject + " 사진 분석이 끝났어요. 기록에서 확인해보세요.";

            notificationWriter.write(
                    event.actorUserId(),
                    NotificationType.PHOTO_ORGANIZED,
                    NotificationType.PHOTO_ORGANIZED.getLabel(),
                    body,
                    "RECORD",
                    event.photoId());

        } catch (Exception e) {
            log.warn("사진 정리 알림 실패 photoId={}", event.photoId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(CountryAcquiredEvent event) {

        try {
            String countryName = countryInfoRepository.findById(event.countryInfoId())
                    .map(CountryInfoEntity::getCountryName)
                    .orElse(null);
            if (countryName == null) {
                return;
            }

            notificationWriter.write(
                    event.actorUserId(),
                    NotificationType.COUNTRY_ACQUIRED,
                    NotificationType.COUNTRY_ACQUIRED.getLabel(),
                    countryName + " 방문이 세계지도에 기록됐어요.",
                    "WORLD_MAP",
                    event.countryInfoId());

        } catch (Exception e) {
            log.warn("국가 획득 알림 실패 countryInfoId={}", event.countryInfoId(), e);
        }
    }
}
