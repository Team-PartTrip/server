package com.example.PartTrip.tripcard.service.impl;

import com.example.PartTrip.tripcard.service.TripCardCloseService;
import com.example.PartTrip.tripcard.service.TripCardGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    /**
     * 종료일이 지난 카드를 잠근다. 이 시점부터 사진을 붙이거나 지울 수 없다.
     *
     * 예전에는 여기서 세계지도 국가 획득까지 했다. 지역은 카드가 생길 때
     * 이미 기록되므로(#162) 여행이 끝난 뒤에 더 할 일이 없다.
     */
    @Override
    public int closeCardsBefore(LocalDate date) {
        return tripCardCloseService.closeCardsBefore(date).size();
    }
}
