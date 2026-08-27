package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.CountryInfoResponseDto;
import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        if (keyword == null || keyword.isBlank()) {
            return getCountries();
        }
        return countryInfoRepository.searchByCountryOrCity(keyword.trim()).stream()
                .limit(20)
                .map(c -> new CountryInfoResponseDto(
                        c.getCountryInfoId(), c.getCountryName(), c.getCityName(),
                        c.getImageUrl(), c.getSummary()))
                .toList();
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
