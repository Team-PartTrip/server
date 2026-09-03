package com.example.PartTrip.tripcard.service;

import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.service.impl.TripCardGeneratorServiceImpl;
import com.example.PartTrip.worldmap.service.WorldMapService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripCardGeneratorTest {

    @Mock
    private TripCardCloseService tripCardCloseService;
    @Mock
    private WorldMapService worldMapService;
    @InjectMocks
    private TripCardGeneratorServiceImpl tripCardGenerator;

    @Test
    void keepsClosingResultWhenCountryAcquisitionFails() {
        LocalDate today = LocalDate.of(2026, 9, 2);
        TripCardEntity card = TripCardEntity.builder()
                .tripCardId(10L)
                .userId("traveler")
                .dateOver(true)
                .build();
        when(tripCardCloseService.closeCardsBefore(today)).thenReturn(List.of(card));
        doThrow(new IllegalArgumentException("국가 정보 없음"))
                .when(worldMapService).acquireCountry("traveler", 10L);

        int closedCount = tripCardGenerator.closeCardsBefore(today);

        assertThat(closedCount).isEqualTo(1);
        assertThat(card.isDateOver()).isTrue();
        verify(tripCardCloseService).closeCardsBefore(today);
        verify(worldMapService).acquireCountry("traveler", 10L);
    }
}
