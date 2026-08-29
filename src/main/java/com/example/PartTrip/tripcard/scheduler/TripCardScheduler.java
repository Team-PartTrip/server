package com.example.PartTrip.tripcard.scheduler;

import com.example.PartTrip.notification.event.TripCardCreatedEvent;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 여행 카드 자동 생성 · 종료 처리 (Func-003-01)
//
// 여행 카드는 사용자가 만드는 게 아니라 여행 시작일에 저절로 생긴다.
// 여행 기간에는 사진을 붙일 수 있고, 종료일이 지나면 dateOver 를 true 로 바꿔 수정을 막는다.
//
// 생성 시 TripCardCreatedEvent 를 발행한다. 이걸 기다리는 곳이 둘이다.
//   - 알림: RecordNotificationListener 가 "여행카드 생성" 알림을 만든다
//   - 세계지도: 국가 획득 처리 (WorldMapService)
@Slf4j
@Component
@RequiredArgsConstructor
public class TripCardScheduler {

    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final TripCardRepository tripCardRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 매일 새벽 1시. 하루 한 번이면 충분하다.
    // 값을 밖으로 뺀 것은 시연이나 테스트에서 짧게 돌리기 위해서다.
    @Scheduled(cron = "${part-trip.trip-card.create-cron:0 0 1 * * *}")
    public void createTodaysCards() {
        try {
            int created = createCardsFor(LocalDate.now());
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
            int closed = closeCardsBefore(LocalDate.now());
            if (closed > 0) {
                log.info("여행 종료 처리 {}건", closed);
            }
        } catch (Exception e) {
            log.warn("여행 종료 처리 실패", e);
        }
    }

    /** 그 날 출발하는 계획의 멤버 전원에게 카드를 만든다. 이미 있으면 건너뛴다. */
    @Transactional
    public int createCardsFor(LocalDate date) {

        List<GroupTravelPlanEntity> plans =
                groupTravelPlanRepository.findByStartDate(date);

        int created = 0;

        for (GroupTravelPlanEntity plan : plans) {
            for (GroupMemberEntity member : groupMemberRepository.findByGroupIdOrderByJoinedAtAsc(plan.getGroupId())) {

                // 같은 계획으로 두 번 만들지 않는다
                if (tripCardRepository.findByPlanIdAndUserId(plan.getPlanId(), member.getUserId()).isPresent()) {
                    continue;
                }

                TripCardEntity saved = tripCardRepository.save(newCard(plan, member.getUserId()));
                created++;

                // 알림과 세계지도가 이 이벤트를 기다린다
                eventPublisher.publishEvent(
                        new TripCardCreatedEvent(saved.getTripCardId(), member.getUserId()));
            }
        }

        return created;
    }

    /** 종료일이 지난 카드를 잠근다. 이 시점부터 수정할 수 없다. */
    @Transactional
    public int closeCardsBefore(LocalDate date) {

        List<TripCardEntity> finished =
                tripCardRepository.findByDateOverFalseAndEndDateBefore(date);

        for (TripCardEntity card : finished) {
            card.setDateOver(true);
        }

        return finished.size();
    }

    private TripCardEntity newCard(GroupTravelPlanEntity plan, String userId) {

        TripCardEntity card = new TripCardEntity();
        card.setUserId(userId);
        card.setPlanId(plan.getPlanId());
        // 계획에 제목이 없으면 도시 이름으로 대신한다 (title 은 not null)
        card.setTitle(plan.getTravelTitle() != null ? plan.getTravelTitle() : plan.getCityName());
        card.setCountryName(plan.getCountryName());
        card.setCityName(plan.getCityName());
        card.setStartDate(plan.getStartDate());
        card.setEndDate(plan.getEndDate());
        card.setDateOver(false);
        card.setCreatedAt(LocalDateTime.now());

        return card;
    }
}
