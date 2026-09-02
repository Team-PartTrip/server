package com.example.PartTrip.tripcard.service;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripCardCloseService {

    private final TripCardRepository tripCardRepository;

    /**
     * 종료 대상 카드를 먼저 별도 트랜잭션에서 확정한다.
     * 이후 세계지도 획득이 실패해도 이미 커밋된 카드 종료 상태는 유지된다.
     */
    @Transactional
    public List<TripCardEntity> closeCardsBefore(LocalDate date) {
        List<TripCardEntity> finished =
                tripCardRepository.findByDateOverFalseAndEndDateBefore(date);
        finished.forEach(card -> card.setDateOver(true));
        return finished;
    }
}
