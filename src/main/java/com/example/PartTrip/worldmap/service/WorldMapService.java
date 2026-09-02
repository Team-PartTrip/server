package com.example.PartTrip.worldmap.service;

import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import com.example.PartTrip.notification.event.CountryAcquiredEvent;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import com.example.PartTrip.worldmap.dto.response.AcquireCountryResponseDto;
import com.example.PartTrip.worldmap.dto.response.ContinentProgressDto;
import com.example.PartTrip.worldmap.dto.response.WorldMapCountryResponseDto;
import com.example.PartTrip.worldmap.dto.response.WorldMapResponseDto;
import com.example.PartTrip.worldmap.dto.response.WorldMapStatsResponseDto;
import com.example.PartTrip.worldmap.dto.response.WorldMapTripSummaryDto;
import com.example.PartTrip.worldmap.dto.response.WorldMapVisitedCountryDto;
import com.example.PartTrip.worldmap.entity.VisitedCountryEntity;
import com.example.PartTrip.worldmap.entity.VisitedCountryTripEntity;
import com.example.PartTrip.worldmap.repository.VisitedCountryRepository;
import com.example.PartTrip.worldmap.repository.VisitedCountryTripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorldMapService {

    private final VisitedCountryRepository visitedCountryRepository;
    private final VisitedCountryTripRepository visitedCountryTripRepository;
    private final CountryInfoRepository countryInfoRepository;
    private final TripCardRepository tripCardRepository;
    private final CountryMetadataResolver countryMetadataResolver;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public WorldMapResponseDto getMyWorldMap(String userId) {
        List<VisitedCountryEntity> visitedCountries =
                visitedCountryRepository.findByUserIdOrderByFirstVisitedAtAsc(userId);
        Map<Long, CountryInfoEntity> countryById = countryInfoRepository
                .findAllById(visitedCountries.stream().map(VisitedCountryEntity::getCountryInfoId).toList())
                .stream()
                .collect(Collectors.toMap(CountryInfoEntity::getCountryInfoId, Function.identity()));
        Map<Long, List<VisitedCountryTripEntity>> tripsByCountry = findTripsByCountry(visitedCountries);

        List<WorldMapVisitedCountryDto> visited = visitedCountries.stream()
                .map(country -> toVisitedCountry(
                        country,
                        requireCountry(countryById, country.getCountryInfoId()),
                        tripsByCountry.getOrDefault(country.getVisitedCountryId(), List.of())))
                .toList();

        return WorldMapResponseDto.builder()
                .totalCountries(CountryMetadataResolver.TOTAL_COUNTRY_COUNT)
                .visited(visited)
                .build();
    }

    @Transactional
    public AcquireCountryResponseDto acquireCountry(Long tripId, String userId) {
        TripCardEntity card = tripCardRepository.findByTripCardIdAndUserId(tripId, userId)
                .orElseThrow(() -> new IllegalArgumentException("여행 기록을 찾을 수 없습니다."));
        if (!card.isDateOver()) {
            throw new IllegalArgumentException("종료된 여행만 방문 국가로 등록할 수 있습니다.");
        }
        return acquireCompletedTrip(card);
    }

    @Transactional
    public AcquireCountryResponseDto acquireCompletedTrip(TripCardEntity card) {
        String countryCode = countryMetadataResolver.resolveCode(card.getCountryName());
        if (visitedCountryTripRepository.findByTripCardId(card.getTripCardId()).isPresent()) {
            return AcquireCountryResponseDto.builder()
                    .countryCode(countryCode)
                    .isNew(false)
                    .build();
        }

        CountryInfoEntity countryInfo = findOrCreateCountryInfo(card);
        VisitedCountryEntity visitedCountry = visitedCountryRepository
                .findByUserIdAndCountryInfoId(card.getUserId(), countryInfo.getCountryInfoId())
                .orElse(null);
        boolean isNew = visitedCountry == null;

        if (isNew) {
            visitedCountry = new VisitedCountryEntity();
            visitedCountry.setUserId(card.getUserId());
            visitedCountry.setCountryInfoId(countryInfo.getCountryInfoId());
            visitedCountry.setFirstVisitedAt(card.getStartDate());
            visitedCountry.setVisitCount(0);
            visitedCountry.setCreatedAt(LocalDateTime.now());
        }
        visitedCountry.setLastVisitedAt(card.getEndDate());
        visitedCountry.setVisitCount(visitedCountry.getVisitCount() + 1);
        VisitedCountryEntity savedCountry = visitedCountryRepository.save(visitedCountry);

        VisitedCountryTripEntity claim = new VisitedCountryTripEntity();
        claim.setVisitedCountryId(savedCountry.getVisitedCountryId());
        claim.setTripCardId(card.getTripCardId());
        claim.setCityName(card.getCityName());
        claim.setStartDate(card.getStartDate());
        claim.setEndDate(card.getEndDate());
        claim.setCreatedAt(LocalDateTime.now());
        visitedCountryTripRepository.save(claim);

        if (isNew) {
            eventPublisher.publishEvent(new CountryAcquiredEvent(
                    countryInfo.getCountryInfoId(), card.getUserId()));
        }

        return AcquireCountryResponseDto.builder()
                .countryCode(countryCode)
                .isNew(isNew)
                .build();
    }

    @Transactional(readOnly = true)
    public WorldMapCountryResponseDto getCountry(String countryCode, String userId) {
        VisitedCountryEntity visitedCountry = findVisitedCountryByCode(countryCode, userId);
        CountryInfoEntity countryInfo = countryInfoRepository.findById(visitedCountry.getCountryInfoId())
                .orElseThrow(() -> new IllegalArgumentException("국가 정보를 찾을 수 없습니다."));
        List<VisitedCountryTripEntity> claims = visitedCountryTripRepository
                .findByVisitedCountryIdOrderByStartDateAsc(visitedCountry.getVisitedCountryId());

        return WorldMapCountryResponseDto.builder()
                .countryName(countryInfo.getCountryName())
                .visitCount(visitedCountry.getVisitCount())
                .cities(distinctCities(claims))
                .trips(claims.stream().map(this::toTripSummary).toList())
                .build();
    }

    @Transactional(readOnly = true)
    public WorldMapStatsResponseDto getStats(String userId) {
        List<VisitedCountryEntity> visitedCountries =
                visitedCountryRepository.findByUserIdOrderByFirstVisitedAtAsc(userId);
        Map<Long, CountryInfoEntity> countryById = countryInfoRepository
                .findAllById(visitedCountries.stream().map(VisitedCountryEntity::getCountryInfoId).toList())
                .stream()
                .collect(Collectors.toMap(CountryInfoEntity::getCountryInfoId, Function.identity()));
        Map<String, Long> acquiredByContinent = visitedCountries.stream()
                .map(country -> requireCountry(countryById, country.getCountryInfoId()))
                .map(CountryInfoEntity::getCountryName)
                .map(countryMetadataResolver::resolveCode)
                .map(countryMetadataResolver::resolveContinent)
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));

        List<ContinentProgressDto> byContinent = countryMetadataResolver.continentTotals().entrySet().stream()
                .map(entry -> ContinentProgressDto.builder()
                        .continent(entry.getKey())
                        .acquiredCount(acquiredByContinent.getOrDefault(entry.getKey(), 0L).intValue())
                        .totalCount(entry.getValue())
                        .build())
                .toList();
        int acquiredCount = visitedCountries.size();

        return WorldMapStatsResponseDto.builder()
                .acquiredCount(acquiredCount)
                .totalCount(CountryMetadataResolver.TOTAL_COUNTRY_COUNT)
                .percentage(acquiredCount * 100.0 / CountryMetadataResolver.TOTAL_COUNTRY_COUNT)
                .byContinent(byContinent)
                .build();
    }

    private CountryInfoEntity findOrCreateCountryInfo(TripCardEntity card) {
        String countryCode = countryMetadataResolver.resolveCode(card.getCountryName());
        String canonicalCountryName = countryMetadataResolver.resolveCountryName(countryCode);

        return countryInfoRepository
                .findFirstByCountryNameIgnoreCaseOrderByCountryInfoIdAsc(canonicalCountryName)
                .or(() -> countryInfoRepository
                        .findFirstByCountryNameIgnoreCaseOrderByCountryInfoIdAsc(card.getCountryName()))
                .orElseGet(() -> {
                    CountryInfoEntity country = new CountryInfoEntity();
                    country.setCountryName(canonicalCountryName);
                    country.setCityName(card.getCityName() == null || card.getCityName().isBlank()
                            ? canonicalCountryName
                            : card.getCityName().trim());
                    return countryInfoRepository.save(country);
                });
    }

    private VisitedCountryEntity findVisitedCountryByCode(String countryCode, String userId) {
        String normalizedCode = countryCode.trim().toUpperCase();
        List<VisitedCountryEntity> visitedCountries =
                visitedCountryRepository.findByUserIdOrderByFirstVisitedAtAsc(userId);
        Map<Long, CountryInfoEntity> countryById = countryInfoRepository
                .findAllById(visitedCountries.stream().map(VisitedCountryEntity::getCountryInfoId).toList())
                .stream()
                .collect(Collectors.toMap(CountryInfoEntity::getCountryInfoId, Function.identity()));

        return visitedCountries.stream()
                .filter(visited -> countryMetadataResolver.resolveCode(
                        requireCountry(countryById, visited.getCountryInfoId()).getCountryName())
                        .equals(normalizedCode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("방문한 국가 기록이 없습니다."));
    }

    private Map<Long, List<VisitedCountryTripEntity>> findTripsByCountry(
            Collection<VisitedCountryEntity> visitedCountries
    ) {
        if (visitedCountries.isEmpty()) {
            return Map.of();
        }
        return visitedCountryTripRepository
                .findByVisitedCountryIdInOrderByStartDateAsc(visitedCountries.stream()
                        .map(VisitedCountryEntity::getVisitedCountryId)
                        .toList())
                .stream()
                .collect(Collectors.groupingBy(VisitedCountryTripEntity::getVisitedCountryId));
    }

    private WorldMapVisitedCountryDto toVisitedCountry(
            VisitedCountryEntity visitedCountry,
            CountryInfoEntity countryInfo,
            List<VisitedCountryTripEntity> claims
    ) {
        return WorldMapVisitedCountryDto.builder()
                .countryCode(countryMetadataResolver.resolveCode(countryInfo.getCountryName()))
                .countryName(countryInfo.getCountryName())
                .visitCount(visitedCountry.getVisitCount())
                .cities(distinctCities(claims))
                .build();
    }

    private List<String> distinctCities(List<VisitedCountryTripEntity> claims) {
        return claims.stream()
                .map(VisitedCountryTripEntity::getCityName)
                .filter(city -> city != null && !city.isBlank())
                .distinct()
                .toList();
    }

    private WorldMapTripSummaryDto toTripSummary(VisitedCountryTripEntity claim) {
        return WorldMapTripSummaryDto.builder()
                .tripId(claim.getTripCardId())
                .cityName(claim.getCityName())
                .startDate(claim.getStartDate())
                .endDate(claim.getEndDate())
                .build();
    }

    private CountryInfoEntity requireCountry(Map<Long, CountryInfoEntity> countries, Long countryInfoId) {
        CountryInfoEntity country = countries.get(countryInfoId);
        if (country == null) {
            throw new IllegalArgumentException("국가 정보를 찾을 수 없습니다.");
        }
        return country;
    }
}
