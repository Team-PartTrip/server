package com.example.PartTrip.main.controller;

import com.example.PartTrip.main.dto.CountryInfoResponseDto;
import com.example.PartTrip.main.service.CountryInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class CountryInfoController {

    private final CountryInfoService countryInfoService;

    // 여행지(국가/도시) 전체 목록 조회 - 여행지 선택 화면 등에서 사용
    @GetMapping("/countries")
    public List<CountryInfoResponseDto> getCountries() {
        return countryInfoService.getCountries();
    }

    // 국가 정보 조회
    @GetMapping("/country-info")
    public CountryInfoResponseDto getCountryInfo(
            @RequestParam String countryName
    ) {
        return countryInfoService.getCountryInfo(countryName);
    }

}
