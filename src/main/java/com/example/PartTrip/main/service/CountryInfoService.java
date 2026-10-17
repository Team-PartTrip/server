package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.CountryInfoResponseDto;
import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
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
     * 여행지 검색 (API-002-03).
     *
     * 도시 이름으로만 찾는다. 나라 이름으로도 찾던 때에는 도시가 없는 줄이
     * 섞여 나왔고, 그걸 고르면 나라 이름이 도시 자리에 저장돼 관광지를
     * 하나도 못 받아왔다. 나라 안의 도시는 /api/main/cities 가 구글에서
     * 찾아준다. 여기는 DB 에 담아둔 여행지만 본다.
     *
     * 검색어를 주면 20개까지만 온다.
     */
    public List<CountryInfoResponseDto> getCountries(String keyword) {
        String normalizedKeyword = normalize(keyword);

        Stream<CountryInfoResponseDto> cities = countryInfoRepository.findAll().stream()
                .filter(c -> c.getCityName() != null && !c.getCityName().isBlank())
                .filter(c -> normalizedKeyword.isBlank()
                        || normalize(c.getCityName()).contains(normalizedKeyword))
                .map(c -> new CountryInfoResponseDto(
                        c.getCountryInfoId(), c.getCountryName(), c.getCityName(),
                        c.getImageUrl(), c.getSummary()))
                .sorted(Comparator.comparing(CountryInfoResponseDto::getCityName));

        if (!normalizedKeyword.isBlank()) {
            cities = cities.limit(20);
        }
        return cities.toList();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
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
