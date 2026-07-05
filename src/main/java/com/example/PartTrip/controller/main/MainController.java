package com.example.PartTrip.controller.main;

import com.example.PartTrip.dto.main.CountryInfoResponseDto;
import com.example.PartTrip.dto.main.FestivalResponseDto;
import com.example.PartTrip.dto.main.PopulationInfoResponseDto;
import com.example.PartTrip.service.main.MainService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainController {

    private final MainService mainService;

    // 국가 정보 조회
    @GetMapping("/country-info")
    public CountryInfoResponseDto getCountryInfo(
            @RequestParam String countryName
    ) {
        return mainService.getCountryInfo(countryName);
    }


    // 인구 구성 조회
    @GetMapping("/population-info")
    public List<PopulationInfoResponseDto> getPopulationInfo(
            @RequestParam String countryName
    ) {
        return mainService.getPopulationInfo(countryName);
    }

    // 축제 조회
    @GetMapping("/festivals")
    public List<FestivalResponseDto> getFestivals(
            @RequestParam String countryName
    ) {
        return mainService.getFestivals(countryName);
    }

}
