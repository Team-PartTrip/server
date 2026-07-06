package com.example.PartTrip.controller.main;

import com.example.PartTrip.dto.main.CountryInfoResponseDto;
import com.example.PartTrip.dto.main.ExchangeRateResponseDto;
import com.example.PartTrip.dto.main.WeatherResponseDto;
import com.example.PartTrip.service.main.CountryInfoService;
import com.example.PartTrip.service.main.ExchangeRateService;
import com.example.PartTrip.service.main.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class CountryInfoController {

    private final CountryInfoService countryInfoService;
    private final ExchangeRateService exchangeRateService;
    private final WeatherService weatherService;

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

    // 환율 조회 (1 현지통화 = ? 원)
    @GetMapping("/exchange-rate")
    public ExchangeRateResponseDto getExchangeRate(
            @RequestParam String countryName
    ) {
        return exchangeRateService.getExchangeRate(countryName);
    }

    // 실시간 날씨 조회
    @GetMapping("/weather")
    public WeatherResponseDto getWeather(
            @RequestParam String countryName
    ) {
        return weatherService.getWeather(countryName);
    }

}