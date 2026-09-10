package com.example.PartTrip.main.controller;

import com.example.PartTrip.main.dto.CitySearchResponseDto;
import com.example.PartTrip.main.dto.CountryInfoResponseDto;
import com.example.PartTrip.main.service.CitySearchService;
import com.example.PartTrip.main.service.CountryInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class CountryInfoController {

    private final CountryInfoService countryInfoService;
    private final CitySearchService citySearchService;

    // 여행지(국가/도시) 전체 목록 조회 - 여행지 선택 화면 등에서 사용
    @GetMapping("/countries")
    public List<CountryInfoResponseDto> getCountries(
            @RequestParam(required = false) String keyword
    ) {
        return countryInfoService.getCountries(keyword);
    }

    // 도시 검색 - country_info 에는 수도만 있어서 따로 받는다.
    // countryName 을 주면 그 나라 안에서만 찾는다.
    @GetMapping("/cities")
    public List<CitySearchResponseDto> searchCities(
            @RequestParam(required = false) String countryName,
            @RequestParam(required = false) String keyword
    ) {
        return citySearchService.search(countryName, keyword);
    }

    // 국가 정보 조회
    @GetMapping("/country-info")
    public CountryInfoResponseDto getCountryInfo(
            @RequestParam String countryName
    ) {
        return countryInfoService.getCountryInfo(countryName);
    }

}
