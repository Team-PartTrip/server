package com.example.PartTrip.tripcard.service;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripCardCloseService {

    private final TripCardRepository tripCardRepository;

    /**
     * 종료 대상 카드를 별도 트랜잭션에서 확정한다.
     * 스케줄러가 뒤에서 무엇을 하든 잠긴 카드는 되돌아가지 않는다.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<TripCardEntity> closeCardsBefore(LocalDate date) {
        List<TripCardEntity> finished =
                tripCardRepository.findByDateOverFalseAndEndDateBefore(date);
        finished.forEach(card -> card.setDateOver(true));
        return finished;
    }
}
