package com.example.PartTrip.tripcard.service;

import com.example.PartTrip.notification.event.TripCardCreatedEvent;
import com.example.PartTrip.planner.entity.GroupMemberEntity;
import com.example.PartTrip.planner.entity.GroupTravelPlanEntity;
import com.example.PartTrip.planner.repository.GroupMemberRepository;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// 여행 카드 자동 생성 · 종료 처리의 실제 작업.
//
// 스케줄러와 나눈 이유는 @Transactional 때문이다. 같은 클래스 안에서 부르면
// 스프링 프록시를 타지 않아 트랜잭션이 열리지 않고, 종료 처리의 변경 감지가 통째로 무시된다.
@Service
@RequiredArgsConstructor
public class TripCardGenerator {

    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final TripCardRepository tripCardRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 진행 중인 여행의 멤버 전원에게 카드를 만든다. 이미 있으면 건너뛴다.
     *
     * 출발 당일만 보지 않고 '시작했고 아직 안 끝난' 계획을 모두 훑는다.
     * 출발일에 서버가 꺼져 있으면 그 여행은 영영 카드가 안 생기기 때문이다.
     */
    @Transactional
    public int createCardsFor(LocalDate date) {

        List<GroupTravelPlanEntity> plans = groupTravelPlanRepository
                .findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date);

        int created = 0;

        for (GroupTravelPlanEntity plan : plans) {

            List<String> memberIds = groupMemberRepository
                    .findByGroupIdOrderByJoinedAtAsc(plan.getGroupId())
                    .stream()
                    .map(GroupMemberEntity::getUserId)
                    .toList();

            if (memberIds.isEmpty()) {
                continue;
            }

            // 멤버 수만큼 조회하지 않고 한 번에 확인한다
            Set<String> alreadyHas = tripCardRepository
                    .findByPlanIdAndUserIdIn(plan.getPlanId(), memberIds)
                    .stream()
                    .map(TripCardEntity::getUserId)
                    .collect(Collectors.toSet());

            for (String userId : memberIds) {
                if (alreadyHas.contains(userId)) {
                    continue;
                }

                TripCardEntity saved = tripCardRepository.save(newCard(plan, userId));
                created++;

                // 알림과 세계지도가 이 이벤트를 기다린다
                eventPublisher.publishEvent(
                        new TripCardCreatedEvent(saved.getTripCardId(), userId));
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
