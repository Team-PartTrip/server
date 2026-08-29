package com.example.PartTrip.tripcard.scheduler;

import com.example.PartTrip.tripcard.service.TripCardGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

// 여행 카드 종료 처리 (Func-003-01)
//
// 카드는 플래너가 확정될 때 PlannerConfirmService 가 만들고
// TripCardCreatedEvent 도 거기서 발행한다. 여기서 또 만들지 않는다.
//
// 여행 기간에는 사진을 붙일 수 있고, 종료일이 지나면 dateOver 를 true 로 바꿔 수정을 막는다.
// 시간으로 발생하는 일이라 이걸 해줄 사람이 따로 없어서 스케줄러가 맡는다.
@Slf4j
@Component
@RequiredArgsConstructor
public class TripCardScheduler {

    private final TripCardGenerator tripCardGenerator;

    // 매일 새벽 1시. 하루 한 번이면 충분하다.
    // 값을 밖으로 뺀 것은 시연이나 테스트에서 짧게 돌리기 위해서다.
    @Scheduled(cron = "${part-trip.trip-card.close-cron:0 0 1 * * *}")
    public void closeFinishedCards() {
        try {
            int closed = tripCardGenerator.closeCardsBefore(LocalDate.now());
            if (closed > 0) {
                log.info("여행 종료 처리 {}건", closed);
            }
        } catch (Exception e) {
            // 스케줄러가 매일 스택트레이스를 남기지 않도록 경고만 남긴다
            log.warn("여행 종료 처리 실패", e);
        }
    }
}
