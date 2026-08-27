package com.example.PartTrip.worldmap.service;

import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import com.example.PartTrip.notification.event.CountryAcquiredEvent;
import com.example.PartTrip.tripcard.entity.TripCardEntity;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import com.example.PartTrip.util.CountryCodeMapper;
import com.example.PartTrip.worldmap.dto.response.AcquireCountryResponseDto;
import com.example.PartTrip.worldmap.dto.response.CountryTravelHistoryResponseDto;
import com.example.PartTrip.worldmap.dto.response.VisitedCountryResponseDto;
import com.example.PartTrip.worldmap.dto.response.WorldMapResponseDto;
import com.example.PartTrip.worldmap.dto.response.WorldMapStatsResponseDto;
import com.example.PartTrip.worldmap.entity.VisitedCountryEntity;
import com.example.PartTrip.worldmap.enums.Continent;
import com.example.PartTrip.worldmap.repository.VisitedCountryRepository;
import com.example.PartTrip.worldmap.support.CountryContinentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorldMapService {

    private final VisitedCountryRepository visitedCountryRepository;
    private final CountryInfoRepository countryInfoRepository;
    private final TripCardRepository tripCardRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public WorldMapResponseDto getWorldMap(String userId) {
        List<VisitedCountryEntity> visitedCountries =
                visitedCountryRepository.findByUserIdOrderByFirstVisitedAtAsc(userId);
        Map<Long, CountryInfoEntity> countryById = findCountriesById(visitedCountries);

        List<VisitedCountryResponseDto> visited = visitedCountries.stream()
                .map(VisitedCountryEntity::getCountryInfoId)
                .map(countryById::get)
                .filter(Objects::nonNull)
                .map(country -> VisitedCountryResponseDto.builder()
                        .countryCode(requireCountryCode(country.getCountryName()))
                        .countryName(country.getCountryName())
                        .build())
                .toList();

        return WorldMapResponseDto.builder()
                .totalCountries(countryInfoRepository.countDistinctCountries())
                .visited(visited)
                .build();
    }

    /**
     * 여행카드가 생성되는 다른 도메인에서도 그대로 호출할 수 있는 국가 획득 진입점이다.
     * tripId는 ERD의 완료 여행 기록인 trip_card.trip_card_id를 의미한다.
     */
    @Transactional
    public AcquireCountryResponseDto acquireCountry(String userId, Long tripId) {
        TripCardEntity trip = tripCardRepository.findByTripCardIdAndUserId(tripId, userId)
                .orElseThrow(() -> new IllegalArgumentException("여행 기록을 찾을 수 없거나 접근 권한이 없습니다."));
        CountryInfoEntity country = countryInfoRepository
                .findByCountryNameIgnoreCase(trip.getCountryName())
                .orElseThrow(() -> new IllegalArgumentException("여행 기록의 국가가 국가 정보에 존재하지 않습니다."));
        String countryCode = requireCountryCode(country.getCountryName());

        List<TripCardEntity> countryTrips = findCountryTrips(userId, country.getCountryName());
        VisitedCountryEntity visitedCountry = visitedCountryRepository
                .findByUserIdAndCountryInfoId(userId, country.getCountryInfoId())
                .orElse(null);
        boolean isNew = visitedCountry == null;

        if (isNew) {
            visitedCountry = new VisitedCountryEntity();
            visitedCountry.setUserId(userId);
            visitedCountry.setCountryInfoId(country.getCountryInfoId());
            visitedCountry.setCreatedAt(LocalDateTime.now());
        }

        visitedCountry.setFirstVisitedAt(countryTrips.get(countryTrips.size() - 1).getStartDate());
        visitedCountry.setLastVisitedAt(countryTrips.get(0).getEndDate());
        visitedCountry.setVisitCount(countryTrips.size());
        visitedCountryRepository.save(visitedCountry);

        if (isNew) {
            eventPublisher.publishEvent(new CountryAcquiredEvent(country.getCountryInfoId(), userId));
        }

        return AcquireCountryResponseDto.builder()
                .countryCode(countryCode)
                .isNew(isNew)
                .build();
    }

    @Transactional(readOnly = true)
    public CountryTravelHistoryResponseDto getCountryHistory(String userId, String countryCode) {
        CountryInfoEntity country = findCountry(countryCode);
        if (!visitedCountryRepository.existsByUserIdAndCountryInfoId(
                userId, country.getCountryInfoId())) {
            throw new IllegalArgumentException("획득하지 않은 국가입니다.");
        }

        List<TripCardEntity> trips = findCountryTrips(userId, country.getCountryName());
        List<String> cities = trips.stream()
                .map(TripCardEntity::getCityName)
                .filter(Objects::nonNull)
                .filter(city -> !city.isBlank())
                .distinct()
                .toList();
        List<CountryTravelHistoryResponseDto.TripResponseDto> tripResponses = trips.stream()
                .map(trip -> CountryTravelHistoryResponseDto.TripResponseDto.builder()
                        .tripId(trip.getTripCardId())
                        .cityName(trip.getCityName())
                        .startDate(trip.getStartDate())
                        .endDate(trip.getEndDate())
                        .build())
                .toList();

        return CountryTravelHistoryResponseDto.builder()
                .countryCode(countryCode.toUpperCase())
                .countryName(country.getCountryName())
                .visitCount(trips.size())
                .cities(cities)
                .trips(tripResponses)
                .build();
    }

    @Transactional(readOnly = true)
    public WorldMapStatsResponseDto getStats(String userId) {
        List<CountryInfoEntity> countries = distinctCountries(countryInfoRepository.findAll());
        Map<Long, CountryInfoEntity> countryById = countries.stream()
                .collect(Collectors.toMap(CountryInfoEntity::getCountryInfoId, Function.identity()));
        List<VisitedCountryEntity> visitedCountries =
                visitedCountryRepository.findByUserIdOrderByFirstVisitedAtAsc(userId);

        Map<Continent, Long> totals = countByContinent(countries);
        Map<Continent, Long> acquired = countByContinent(visitedCountries.stream()
                .map(VisitedCountryEntity::getCountryInfoId)
                .map(countryById::get)
                .filter(Objects::nonNull)
                .toList());
        List<WorldMapStatsResponseDto.ContinentStatsResponseDto> byContinent = totals.entrySet().stream()
                .filter(entry -> entry.getKey() != Continent.OTHER)
                .map(entry -> WorldMapStatsResponseDto.ContinentStatsResponseDto.builder()
                        .continent(entry.getKey())
                        .acquiredCount(acquired.getOrDefault(entry.getKey(), 0L))
                        .totalCount(entry.getValue())
                        .build())
                .toList();

        long totalCount = countries.size();
        long acquiredCount = visitedCountries.stream()
                .map(VisitedCountryEntity::getCountryInfoId)
                .filter(countryById::containsKey)
                .distinct()
                .count();
        BigDecimal percentage = totalCount == 0
                ? BigDecimal.ZERO.setScale(2)
                : BigDecimal.valueOf(acquiredCount * 100.0 / totalCount)
                        .setScale(2, RoundingMode.HALF_UP);

        return WorldMapStatsResponseDto.builder()
                .acquiredCount(acquiredCount)
                .totalCount(totalCount)
                .percentage(percentage)
                .byContinent(byContinent)
                .build();
    }

    private CountryInfoEntity findCountry(String countryCode) {
        String countryName = CountryCodeMapper.getCountryName(countryCode);
        if (countryName == null) {
            throw new IllegalArgumentException("존재하지 않는 국가 코드입니다.");
        }
        return countryInfoRepository.findByCountryNameIgnoreCase(countryName)
                .orElseThrow(() -> new IllegalArgumentException("국가 정보가 존재하지 않습니다."));
    }

    private List<TripCardEntity> findCountryTrips(String userId, String countryName) {
        List<TripCardEntity> trips = tripCardRepository
                .findByUserIdAndCountryNameIgnoreCaseOrderByStartDateDesc(userId, countryName);
        if (trips.isEmpty()) {
            throw new IllegalArgumentException("해당 국가의 여행 기록을 찾을 수 없습니다.");
        }
        return trips;
    }

    private Map<Long, CountryInfoEntity> findCountriesById(
            List<VisitedCountryEntity> visitedCountries
    ) {
        List<Long> ids = visitedCountries.stream()
                .map(VisitedCountryEntity::getCountryInfoId)
                .distinct()
                .toList();
        return countryInfoRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(CountryInfoEntity::getCountryInfoId, Function.identity()));
    }

    private List<CountryInfoEntity> distinctCountries(List<CountryInfoEntity> countries) {
        return List.copyOf(countries.stream().collect(Collectors.toMap(
                CountryInfoEntity::getCountryName,
                Function.identity(),
                (first, ignored) -> first,
                LinkedHashMap::new
        )).values());
    }

    private Map<Continent, Long> countByContinent(List<CountryInfoEntity> countries) {
        Map<Continent, Long> counts = new EnumMap<>(Continent.class);
        for (CountryInfoEntity country : countries) {
            Continent continent = CountryContinentMapper.getContinent(
                    CountryCodeMapper.getCountryCode(country.getCountryName())
            );
            counts.merge(continent, 1L, Long::sum);
        }
        return counts;
    }

    private String requireCountryCode(String countryName) {
        String countryCode = CountryCodeMapper.getCountryCode(countryName);
        if (countryCode == null) {
            throw new IllegalArgumentException("국가 코드가 등록되지 않은 국가입니다: " + countryName);
        }
        return countryCode;
    }
}
