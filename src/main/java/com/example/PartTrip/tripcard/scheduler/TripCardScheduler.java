package com.example.PartTrip.tripcard.scheduler;

import com.example.PartTrip.tripcard.service.TripCardGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

// 여행 카드 자동 생성 · 종료 처리 (Func-003-01)
//
// 여행 카드는 사용자가 만드는 게 아니라 여행 시작일에 저절로 생긴다.
// 여행 기간에는 사진을 붙일 수 있고, 종료일이 지나면 dateOver 를 true 로 바꿔 수정을 막는다.
//
// 생성 시 TripCardCreatedEvent 를 발행한다. 이걸 기다리는 곳이 둘이다.
//   - 알림: RecordNotificationListener 가 "여행카드 생성" 알림을 만든다
//   - 세계지도: 국가 획득 처리
//
// 실제 작업은 TripCardGenerator 가 한다. 여기서 직접 하면 @Transactional 이 안 걸린다.
@Slf4j
@Component
@RequiredArgsConstructor
public class TripCardScheduler {

    private final TripCardGenerator tripCardGenerator;

    // 매일 새벽 1시. 하루 한 번이면 충분하다.
    // 값을 밖으로 뺀 것은 시연이나 테스트에서 짧게 돌리기 위해서다.
    @Scheduled(cron = "${part-trip.trip-card.create-cron:0 0 1 * * *}")
    public void createTodaysCards() {
        try {
            int created = tripCardGenerator.createCardsFor(LocalDate.now());
            if (created > 0) {
                log.info("여행 카드 자동 생성 {}건", created);
            }
        } catch (Exception e) {
            // 스케줄러가 매일 스택트레이스를 남기지 않도록 경고만 남긴다
            log.warn("여행 카드 자동 생성 실패", e);
        }
    }

    @Scheduled(cron = "${part-trip.trip-card.close-cron:0 10 1 * * *}")
    public void closeFinishedCards() {
        try {
            int closed = tripCardGenerator.closeCardsBefore(LocalDate.now());
            if (closed > 0) {
                log.info("여행 종료 처리 {}건", closed);
            }
        } catch (Exception e) {
            log.warn("여행 종료 처리 실패", e);
        }
    }
}
