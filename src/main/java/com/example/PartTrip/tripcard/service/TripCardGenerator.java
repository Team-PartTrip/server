package com.example.PartTrip.tripcard.service;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import com.example.PartTrip.worldmap.service.WorldMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// 여행 카드 종료 처리.
//
// 카드 자체는 플래너가 확정될 때 PlannerConfirmService 가 만든다.
// 여기서는 여행이 끝난 카드를 잠그는 일만 한다.
//
// 스케줄러와 클래스를 나눈 이유는 @Transactional 때문이다. 같은 클래스 안에서 부르면
// 스프링 프록시를 타지 않아 트랜잭션이 열리지 않고, 변경 감지가 통째로 무시된다.
@Service
@RequiredArgsConstructor
@Slf4j
public class TripCardGenerator {

    private final TripCardRepository tripCardRepository;
    private final WorldMapService worldMapService;

    /** 종료일이 지난 카드를 잠근다. 이 시점부터 사진을 붙이거나 지울 수 없다. */
    @Transactional
    public int closeCardsBefore(LocalDate date) {

        List<TripCardEntity> finished =
                tripCardRepository.findByDateOverFalseAndEndDateBefore(date);

        for (TripCardEntity card : finished) {
            card.setDateOver(true);
            try {
                worldMapService.acquireCompletedTrip(card);
            } catch (IllegalArgumentException exception) {
                log.warn("여행 카드를 종료했지만 세계지도 국가 획득은 처리하지 못했습니다. tripCardId={}",
                        card.getTripCardId(), exception);
            }
        }

        return finished.size();
    }
}
