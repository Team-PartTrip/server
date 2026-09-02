package com.example.PartTrip.tripcard.service;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripCardCloseServiceTest {

    @Mock
    private TripCardRepository tripCardRepository;
    @InjectMocks
    private TripCardCloseService tripCardCloseService;

    @Test
    void marksFinishedCardsAsClosedBeforeCountryAcquisition() {
        LocalDate today = LocalDate.of(2026, 9, 2);
        TripCardEntity card = TripCardEntity.builder().dateOver(false).build();
        when(tripCardRepository.findByDateOverFalseAndEndDateBefore(today))
                .thenReturn(List.of(card));

        List<TripCardEntity> result = tripCardCloseService.closeCardsBefore(today);

        assertThat(result).containsExactly(card);
        assertThat(card.isDateOver()).isTrue();
    }
}
