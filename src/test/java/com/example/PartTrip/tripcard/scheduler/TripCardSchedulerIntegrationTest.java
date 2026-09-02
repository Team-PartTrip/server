package com.example.PartTrip.tripcard.scheduler;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import com.example.PartTrip.tripcard.service.TripCardCloseService;
import com.example.PartTrip.tripcard.service.impl.TripCardGeneratorServiceImpl;
import com.example.PartTrip.worldmap.service.WorldMapService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@DataJpaTest
@Import({TripCardScheduler.class, TripCardGeneratorServiceImpl.class, TripCardCloseService.class})
class TripCardSchedulerIntegrationTest {

    @Autowired private TripCardScheduler tripCardScheduler;
    @Autowired private TripCardRepository tripCardRepository;
    @MockitoBean private WorldMapService worldMapService;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 국가획득이_실패해도_카드종료는_커밋된다() {
        LocalDate today = LocalDate.now();
        TripCardEntity card = tripCardRepository.save(TripCardEntity.builder()
                .userId("traveler")
                .title("종료된 여행")
                .countryName("일본")
                .cityName("오사카")
                .startDate(today.minusDays(5))
                .endDate(today.minusDays(1))
                .dateOver(false)
                .createdAt(LocalDateTime.now().minusDays(5))
                .build());
        doThrow(new IllegalArgumentException("국가 정보 없음"))
                .when(worldMapService).acquireCountry("traveler", card.getTripCardId());

        tripCardScheduler.closeFinishedCards();

        assertThat(tripCardRepository.findById(card.getTripCardId()))
                .get()
                .extracting(TripCardEntity::isDateOver)
                .isEqualTo(true);
        verify(worldMapService).acquireCountry("traveler", card.getTripCardId());
    }
}
