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
import java.util.Map;
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

    /**
     * 나라·도시 검색 (API-002-03).
     *
     * DB 에 있는 여행지에 ISO 전체 국가를 얹어서 준다. DB 에 없는 나라도
     * 이름으로 찾을 수 있어야 하기 때문이다.
     *
     * 예전에는 후보마다 ISO 250개국을 다시 훑어서(matches) 요청 한 번에
     * 6만 번 가까이 돌았다. 이름 → 표시명은 바뀌지 않으므로 한 번만 만들어
     * 재사용한다.
     */
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

        Stream<CountryInfoResponseDto> isoCountries = ISO_COUNTRIES.stream()
                .filter(iso -> !savedNames.contains(iso.korean())
                        && !savedNames.contains(iso.english()))
                .map(iso -> new CountryInfoResponseDto(
                        null, iso.displayKorean(), null, null, null));

        String normalizedKeyword = normalize(keyword);
        Stream<CountryInfoResponseDto> countries =
                Stream.concat(savedResponses.stream(), isoCountries)
                        .filter(country -> matches(country, normalizedKeyword))
                        .sorted(Comparator
                                .comparing(CountryInfoResponseDto::getCountryName,
                                        Comparator.nullsLast(String::compareTo))
                                .thenComparing(CountryInfoResponseDto::getCityName,
                                        Comparator.nullsLast(String::compareTo)));

        if (!normalizedKeyword.isBlank()) {
            countries = countries.limit(20);
        }
        return countries.toList();
    }

    /** 한 나라의 한글·영문 이름. 프로그램이 도는 동안 바뀌지 않는다 */
    private record IsoCountry(String korean, String english, String displayKorean) {}

    /**
     * ISO 국가 목록은 요청마다 만들 이유가 없다.
     *
     * Locale.getDisplayCountry 는 값을 만들어내는 호출이라, 후보 250개 ×
     * ISO 250개를 돌리면 한 요청에 6만 번 넘게 불렸다.
     */
    private static final List<IsoCountry> ISO_COUNTRIES = Arrays.stream(Locale.getISOCountries())
            .map(code -> Locale.of("", code))
            .map(locale -> new IsoCountry(
                    locale.getDisplayCountry(Locale.KOREAN).trim().toLowerCase(Locale.ROOT),
                    locale.getDisplayCountry(Locale.ENGLISH).trim().toLowerCase(Locale.ROOT),
                    locale.getDisplayCountry(Locale.KOREAN)))
            .toList();

    /** 이름 → 그 나라의 한글·영문 이름. 영문으로 쳐도 한글 이름이 걸리게 한다 */
    private static final Map<String, IsoCountry> ISO_BY_NAME = ISO_COUNTRIES.stream()
            .flatMap(iso -> Stream.of(
                    Map.entry(iso.korean(), iso),
                    Map.entry(iso.english(), iso)))
            .filter(e -> !e.getKey().isBlank())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));

    private boolean matches(CountryInfoResponseDto country, String keyword) {
        if (keyword.isBlank()) {
            return true;
        }
        if (normalize(country.getCountryName()).contains(keyword)
                || normalize(country.getCityName()).contains(keyword)) {
            return true;
        }
        // "france" 로 쳐도 "프랑스" 가 나와야 한다
        IsoCountry iso = ISO_BY_NAME.get(normalize(country.getCountryName()));
        return iso != null
                && (iso.korean().contains(keyword) || iso.english().contains(keyword));
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
