package com.example.PartTrip.worldmap.service;

import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import com.example.PartTrip.notification.event.CountryAcquiredEvent;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import com.example.PartTrip.worldmap.dto.response.AcquireCountryResponseDto;
import com.example.PartTrip.worldmap.entity.VisitedCountryEntity;
import com.example.PartTrip.worldmap.entity.VisitedCountryTripEntity;
import com.example.PartTrip.worldmap.repository.VisitedCountryRepository;
import com.example.PartTrip.worldmap.repository.VisitedCountryTripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WorldMapServiceTest {

    private static final long TRIP_ID = 10L;
    private static final String USER_ID = "traveler";

    @Mock
    private VisitedCountryRepository visitedCountryRepository;
    @Mock
    private VisitedCountryTripRepository visitedCountryTripRepository;
    @Mock
    private CountryInfoRepository countryInfoRepository;
    @Mock
    private TripCardRepository tripCardRepository;
    @Mock
    private CountryMetadataResolver countryMetadataResolver;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private WorldMapService worldMapService;

    private TripCardEntity card;

    @BeforeEach
    void setUp() {
        card = new TripCardEntity();
        card.setTripCardId(TRIP_ID);
        card.setUserId(USER_ID);
        card.setCountryName("일본");
        card.setCityName("도쿄");
        card.setStartDate(LocalDate.of(2026, 8, 1));
        card.setEndDate(LocalDate.of(2026, 8, 5));
    }

    @Test
    void acquiresNewCountryFromCompletedTrip() {
        card.setDateOver(true);
        CountryInfoEntity country = new CountryInfoEntity();
        country.setCountryInfoId(7L);
        country.setCountryName("일본");
        country.setCityName("도쿄");

        given(tripCardRepository.findByTripCardIdAndUserId(TRIP_ID, USER_ID))
                .willReturn(Optional.of(card));
        given(countryMetadataResolver.resolveCode("일본")).willReturn("JP");
        given(countryMetadataResolver.resolveCountryName("JP")).willReturn("일본");
        given(visitedCountryTripRepository.findByTripCardId(TRIP_ID)).willReturn(Optional.empty());
        given(countryInfoRepository.findFirstByCountryNameIgnoreCaseOrderByCountryInfoIdAsc("일본"))
                .willReturn(Optional.of(country));
        given(visitedCountryRepository.findByUserIdAndCountryInfoId(USER_ID, 7L))
                .willReturn(Optional.empty());
        given(visitedCountryRepository.save(any(VisitedCountryEntity.class)))
                .willAnswer(invocation -> {
                    VisitedCountryEntity visited = invocation.getArgument(0);
                    visited.setVisitedCountryId(20L);
                    return visited;
                });

        AcquireCountryResponseDto response = worldMapService.acquireCountry(TRIP_ID, USER_ID);

        assertThat(response.getCountryCode()).isEqualTo("JP");
        assertThat(response.getIsNew()).isTrue();
        verify(visitedCountryTripRepository).save(any(VisitedCountryTripEntity.class));
        verify(eventPublisher).publishEvent(any(CountryAcquiredEvent.class));
    }

    @Test
    void doesNotCountTheSameTripTwice() {
        card.setDateOver(true);
        given(tripCardRepository.findByTripCardIdAndUserId(TRIP_ID, USER_ID))
                .willReturn(Optional.of(card));
        given(countryMetadataResolver.resolveCode("일본")).willReturn("JP");
        given(visitedCountryTripRepository.findByTripCardId(TRIP_ID))
                .willReturn(Optional.of(new VisitedCountryTripEntity()));

        AcquireCountryResponseDto response = worldMapService.acquireCountry(TRIP_ID, USER_ID);

        assertThat(response.getIsNew()).isFalse();
        verify(visitedCountryRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void rejectsTripThatHasNotEnded() {
        card.setDateOver(false);
        given(tripCardRepository.findByTripCardIdAndUserId(TRIP_ID, USER_ID))
                .willReturn(Optional.of(card));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> worldMapService.acquireCountry(TRIP_ID, USER_ID))
                .withMessage("종료된 여행만 방문 국가로 등록할 수 있습니다.");
        verify(visitedCountryRepository, never()).save(any());
    }
}
