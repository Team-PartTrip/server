package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.CountryInfoResponseDto;
import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CountryInfoService {

    private final CountryInfoRepository countryInfoRepository;

    // 여행지(국가/도시) 전체 목록 조회 - 여행지 선택 화면 등에서 사용
    public List<CountryInfoResponseDto> getCountries() {

        List<CountryInfoEntity> countries = countryInfoRepository.findAll(Sort.by("countryName"));

        return countries.stream()
                .map(c -> new CountryInfoResponseDto(
                        c.getCountryInfoId(),
                        c.getCountryName(),
                        c.getCityName(),
                        c.getImageUrl(),
                        c.getSummary()
                ))
                .collect(Collectors.toList());
    }

    public List<CountryInfoResponseDto> getCountries(String keyword) {
        List<CountryInfoEntity> savedCountries = countryInfoRepository.findAll();
        List<CountryInfoResponseDto> savedResponses = savedCountries.stream()
                .map(c -> new CountryInfoResponseDto(
                        c.getCountryInfoId(), c.getCountryName(), c.getCityName(),
                        c.getImageUrl(), c.getSummary()))
                .toList();

        Set<String> savedNames = savedCountries.stream()
                .map(CountryInfoEntity::getCountryName)
                .filter(name -> name != null && !name.isBlank())
                .map(this::normalize)
                .collect(Collectors.toCollection(HashSet::new));

        Stream<CountryInfoResponseDto> isoCountries = Arrays.stream(Locale.getISOCountries())
                .map(code -> Locale.of("", code))
                .filter(locale -> !savedNames.contains(normalize(locale.getDisplayCountry(Locale.KOREAN))))
                .filter(locale -> !savedNames.contains(normalize(locale.getDisplayCountry(Locale.ENGLISH))))
                .map(locale -> new CountryInfoResponseDto(
                        null,
                        locale.getDisplayCountry(Locale.KOREAN),
                        null,
                        null,
                        null
                ));

        String normalizedKeyword = normalize(keyword);
        Stream<CountryInfoResponseDto> countries = Stream.concat(savedResponses.stream(), isoCountries)
                .filter(country -> matches(country, normalizedKeyword))
                .sorted(Comparator
                        .comparing(CountryInfoResponseDto::getCountryName, Comparator.nullsLast(String::compareTo))
                        .thenComparing(CountryInfoResponseDto::getCityName, Comparator.nullsLast(String::compareTo)));

        if (!normalizedKeyword.isBlank()) {
            countries = countries.limit(20);
        }
        return countries
                .toList();
    }

    private boolean matches(CountryInfoResponseDto country, String keyword) {
        if (keyword.isBlank()) {
            return true;
        }
        if (normalize(country.getCountryName()).contains(keyword)
                || normalize(country.getCityName()).contains(keyword)) {
            return true;
        }
        return Arrays.stream(Locale.getISOCountries())
                .map(code -> Locale.of("", code))
                .anyMatch(locale -> {
                    String koreanName = normalize(locale.getDisplayCountry(Locale.KOREAN));
                    String englishName = normalize(locale.getDisplayCountry(Locale.ENGLISH));
                    String savedName = normalize(country.getCountryName());
                    return (koreanName.equals(savedName) || englishName.equals(savedName))
                            && (koreanName.contains(keyword) || englishName.contains(keyword));
                });
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    // 국가 정보 조회
    public CountryInfoResponseDto getCountryInfo(String countryName) {

        CountryInfoEntity country = countryInfoRepository.findByCountryName(countryName)
                .orElseThrow(() -> new IllegalArgumentException("국가 정보를 찾을 수 없습니다."));

        return new CountryInfoResponseDto(
                country.getCountryInfoId(),
                country.getCountryName(),
                country.getCityName(),
                country.getImageUrl(),
                country.getSummary()
        );
    }

    public List<CountryInfoResponseDto> searchCountries(String keyword) {

        return countryInfoRepository
                .findTop20ByCountryNameContainingOrderByCountryNameAsc(keyword)
                .stream()
                .map(c -> new CountryInfoResponseDto(
                        c.getCountryInfoId(),
                        c.getCountryName(),
                        c.getCityName(),
                        c.getImageUrl(),
                        c.getSummary()
                ))
                .toList();
    }
}
