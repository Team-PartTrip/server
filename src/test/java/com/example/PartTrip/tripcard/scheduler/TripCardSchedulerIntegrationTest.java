package com.example.PartTrip.tripcard.scheduler;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import com.example.PartTrip.tripcard.service.TripCardCloseService;
import com.example.PartTrip.tripcard.service.impl.TripCardGeneratorServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TripCardScheduler.class, TripCardGeneratorServiceImpl.class, TripCardCloseService.class})
class TripCardSchedulerIntegrationTest {

    @Autowired private TripCardScheduler tripCardScheduler;
    @Autowired private TripCardRepository tripCardRepository;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 종료일이_지난_카드는_잠기고_커밋된다() {
        LocalDate today = LocalDate.now();
        TripCardEntity card = tripCardRepository.save(TripCardEntity.builder()
                .userId("traveler")
                .title("종료된 여행")
                .regionCode("42")
                .cityName("강릉시")
                .startDate(today.minusDays(5))
                .endDate(today.minusDays(1))
                .dateOver(false)
                .createdAt(LocalDateTime.now().minusDays(5))
                .build());

        tripCardScheduler.closeFinishedCards();

        // 테스트 트랜잭션 밖에서 읽는다. 스케줄러가 실제로 커밋했는지를 본다
        assertThat(tripCardRepository.findById(card.getTripCardId()))
                .get()
                .extracting(TripCardEntity::isDateOver)
                .isEqualTo(true);
    }
}
