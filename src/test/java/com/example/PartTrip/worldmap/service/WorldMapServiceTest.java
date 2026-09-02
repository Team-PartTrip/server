package com.example.PartTrip.worldmap.service;

import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import com.example.PartTrip.notification.event.CountryAcquiredEvent;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import com.example.PartTrip.worldmap.dto.response.AcquireCountryResponseDto;
import com.example.PartTrip.worldmap.dto.response.CountryTravelHistoryResponseDto;
import com.example.PartTrip.worldmap.dto.response.WorldMapStatsResponseDto;
import com.example.PartTrip.worldmap.entity.VisitedCountryEntity;
import com.example.PartTrip.worldmap.enums.Continent;
import com.example.PartTrip.worldmap.repository.VisitedCountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorldMapServiceTest {

    @Mock
    private VisitedCountryRepository visitedCountryRepository;
    @Mock
    private CountryInfoRepository countryInfoRepository;
    @Mock
    private TripCardRepository tripCardRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private WorldMapService worldMapService;

    @Test
    void returnsEmptyWorldMapForUserWithoutVisits() {
        when(visitedCountryRepository.findByUserIdOrderByFirstVisitedAtAsc("user1"))
                .thenReturn(List.of());
        when(countryInfoRepository.findAllById(List.of())).thenReturn(List.of());
        when(countryInfoRepository.countDistinctCountries()).thenReturn(195L);

        var result = worldMapService.getWorldMap("user1");

        assertThat(result.getTotalCountries()).isEqualTo(195);
        assertThat(result.getVisited()).isEmpty();
    }

    @Test
    void acquiresCountryFromOwnedTripAndPublishesNotificationOnlyOnce() {
        CountryInfoEntity japan = country(10L, "일본");
        TripCardEntity oldTrip = trip(1L, "user1", "일본", "도쿄",
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 5));
        TripCardEntity recentTrip = trip(2L, "user1", "일본", "오사카",
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 4));
        when(tripCardRepository.findByTripCardIdAndUserId(2L, "user1"))
                .thenReturn(Optional.of(recentTrip));
        when(countryInfoRepository.findFirstByCountryNameIgnoreCaseOrderByCountryInfoIdAsc("일본"))
                .thenReturn(Optional.of(japan));
        when(tripCardRepository.findByUserIdAndCountryNameIgnoreCaseAndDateOverTrueOrderByStartDateDesc(
                "user1", "일본"))
                .thenReturn(List.of(recentTrip, oldTrip));
        when(visitedCountryRepository.findByUserIdAndCountryInfoId("user1", 10L))
                .thenReturn(Optional.empty());

        AcquireCountryResponseDto result = worldMapService.acquireCountry("user1", 2L);

        assertThat(result.getCountryCode()).isEqualTo("JP");
        assertThat(result.isNew()).isTrue();
        ArgumentCaptor<VisitedCountryEntity> saved =
                ArgumentCaptor.forClass(VisitedCountryEntity.class);
        verify(visitedCountryRepository).save(saved.capture());
        assertThat(saved.getValue().getVisitCount()).isEqualTo(2);
        assertThat(saved.getValue().getFirstVisitedAt()).isEqualTo(oldTrip.getStartDate());
        assertThat(saved.getValue().getLastVisitedAt()).isEqualTo(recentTrip.getEndDate());
        verify(eventPublisher).publishEvent(any(CountryAcquiredEvent.class));
    }

    @Test
    void reacquiringExistingCountryDoesNotDuplicateOrNotify() {
        CountryInfoEntity japan = country(10L, "일본");
        TripCardEntity trip = trip(1L, "user1", "일본", "도쿄",
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5));
        VisitedCountryEntity existing = visited("user1", 10L);
        when(tripCardRepository.findByTripCardIdAndUserId(1L, "user1"))
                .thenReturn(Optional.of(trip));
        when(countryInfoRepository.findFirstByCountryNameIgnoreCaseOrderByCountryInfoIdAsc("일본"))
                .thenReturn(Optional.of(japan));
        when(tripCardRepository.findByUserIdAndCountryNameIgnoreCaseAndDateOverTrueOrderByStartDateDesc(
                "user1", "일본"))
                .thenReturn(List.of(trip));
        when(visitedCountryRepository.findByUserIdAndCountryInfoId("user1", 10L))
                .thenReturn(Optional.of(existing));

        AcquireCountryResponseDto result = worldMapService.acquireCountry("user1", 1L);

        assertThat(result.isNew()).isFalse();
        verify(visitedCountryRepository).save(existing);
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void rejectsMissingOrAnotherUsersTrip() {
        when(tripCardRepository.findByTripCardIdAndUserId(99L, "user1"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> worldMapService.acquireCountry("user1", 99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("접근 권한");
    }

    @Test
    void rejectsTripThatHasNotEnded() {
        TripCardEntity ongoingTrip = trip(1L, "user1", "일본", "도쿄",
                LocalDate.now(), LocalDate.now().plusDays(1));
        ongoingTrip.setDateOver(false);
        when(tripCardRepository.findByTripCardIdAndUserId(1L, "user1"))
                .thenReturn(Optional.of(ongoingTrip));

        assertThatThrownBy(() -> worldMapService.acquireCountry("user1", 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("종료된 여행");
        verify(visitedCountryRepository, never()).save(any());
    }

    @Test
    void returnsOwnedCountryHistoryWithDistinctCitiesInRecentOrder() {
        CountryInfoEntity japan = country(10L, "일본");
        TripCardEntity recent = trip(2L, "user1", "일본", "도쿄",
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 5));
        TripCardEntity old = trip(1L, "user1", "일본", "도쿄",
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 5));
        when(countryInfoRepository.findFirstByCountryNameIgnoreCaseOrderByCountryInfoIdAsc("일본"))
                .thenReturn(Optional.of(japan));
        when(visitedCountryRepository.existsByUserIdAndCountryInfoId("user1", 10L))
                .thenReturn(true);
        when(tripCardRepository.findByUserIdAndCountryNameIgnoreCaseAndDateOverTrueOrderByStartDateDesc(
                "user1", "일본"))
                .thenReturn(List.of(recent, old));

        CountryTravelHistoryResponseDto result =
                worldMapService.getCountryHistory("user1", "jp");

        assertThat(result.getVisitCount()).isEqualTo(2);
        assertThat(result.getCities()).containsExactly("도쿄");
        assertThat(result.getTrips()).extracting(
                CountryTravelHistoryResponseDto.TripResponseDto::getTripCardId)
                .containsExactly(2L, 1L);
    }

    @Test
    void calculatesZeroAndContinentStatsFromCountryMaster() {
        CountryInfoEntity korea = country(1L, "한국");
        CountryInfoEntity japan = country(2L, "일본");
        CountryInfoEntity france = country(3L, "프랑스");
        when(countryInfoRepository.findAll()).thenReturn(List.of(korea, japan, france));
        when(visitedCountryRepository.findByUserIdOrderByFirstVisitedAtAsc("user1"))
                .thenReturn(List.of(visited("user1", 2L)));

        WorldMapStatsResponseDto result = worldMapService.getStats("user1");

        assertThat(result.getAcquiredCount()).isEqualTo(1);
        assertThat(result.getTotalCount()).isEqualTo(3);
        assertThat(result.getPercentage()).isEqualByComparingTo("33.33");
        assertThat(result.getByContinent())
                .filteredOn(stat -> stat.getContinent() == Continent.ASIA)
                .singleElement()
                .satisfies(stat -> {
                    assertThat(stat.getAcquiredCount()).isEqualTo(1);
                    assertThat(stat.getTotalCount()).isEqualTo(2);
                });
    }

    @Test
    void returnsZeroPercentageWhenCountryMasterIsEmpty() {
        when(countryInfoRepository.findAll()).thenReturn(List.of());
        when(visitedCountryRepository.findByUserIdOrderByFirstVisitedAtAsc("user1"))
                .thenReturn(List.of());

        WorldMapStatsResponseDto result = worldMapService.getStats("user1");

        assertThat(result.getAcquiredCount()).isZero();
        assertThat(result.getTotalCount()).isZero();
        assertThat(result.getPercentage()).isEqualByComparingTo("0.00");
        assertThat(result.getByContinent()).isEmpty();
    }

    @Test
    void rejectsUnknownCountryCode() {
        assertThatThrownBy(() -> worldMapService.getCountryHistory("user1", "ZZ"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("국가 코드");
    }

    private CountryInfoEntity country(Long id, String name) {
        CountryInfoEntity country = new CountryInfoEntity();
        country.setCountryInfoId(id);
        country.setCountryName(name);
        country.setCityName("capital");
        return country;
    }

    private TripCardEntity trip(
            Long id,
            String userId,
            String countryName,
            String cityName,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return TripCardEntity.builder()
                .tripCardId(id)
                .userId(userId)
                .countryName(countryName)
                .cityName(cityName)
                .startDate(startDate)
                .endDate(endDate)
                .dateOver(true)
                .build();
    }

    private VisitedCountryEntity visited(String userId, Long countryInfoId) {
        VisitedCountryEntity visited = new VisitedCountryEntity();
        visited.setUserId(userId);
        visited.setCountryInfoId(countryInfoId);
        return visited;
    }
}
