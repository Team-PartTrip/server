package com.example.PartTrip.tripcard.service.impl;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.service.TripCardCloseService;
import com.example.PartTrip.worldmap.service.WorldMapService;
import com.example.PartTrip.tripcard.service.TripCardGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 여행 카드 종료 처리.
//
// 카드 자체는 플래너가 확정될 때 PlannerConfirmService 가 만든다.
// 여기서는 여행이 끝난 카드를 잠그는 일만 한다.
//
// 스케줄러와 클래스를 나눈 이유는 @Transactional 때문이다. 같은 클래스 안에서 부르면
// 스프링 프록시를 타지 않아 트랜잭션이 열리지 않고, 변경 감지가 통째로 무시된다.
@Slf4j
@Service
@RequiredArgsConstructor
public class TripCardGeneratorServiceImpl implements TripCardGeneratorService {

    private final TripCardCloseService tripCardCloseService;
    private final WorldMapService worldMapService;

    /**
     * 종료일이 지난 카드를 잠그고, 그 여행의 국가를 지도에 채운다.
     *
     * 국가 획득은 종료된 여행만 된다(WorldMapService). 확정 시점에는 아직
     * 다녀오기 전이라 부를 수 없어서, 여기가 유일하게 부를 수 있는 자리다.
     *
     * 잠그는 것과 획득을 한 트랜잭션에 두면, 한 사람의 획득이 실패했을 때
     * 그날 잠긴 카드가 전부 되돌아간다. 카드는 먼저 잠그고 획득은 건별로
     * 넘어간다.
     */
    @Override
    public int closeCardsBefore(LocalDate date) {
        List<TripCardEntity> finished = tripCardCloseService.closeCardsBefore(date);

        for (TripCardEntity card : finished) {
            try {
                worldMapService.acquireCountry(card.getUserId(), card.getTripCardId());
            } catch (Exception e) {
                // 국가 정보에 없는 나라 등은 지도만 못 채울 뿐이다.
                // 여기서 멈추면 뒤 카드들이 통째로 밀린다.
                log.warn("국가 획득 실패 (tripCardId={}): {}",
                        card.getTripCardId(), e.getMessage());
            }
        }

        return finished.size();
    }
}
