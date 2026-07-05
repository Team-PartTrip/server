package com.example.PartTrip.controller.main;

import com.example.PartTrip.dto.main.CountryInfoResponseDto;
import com.example.PartTrip.service.main.CountryInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class CountryInfoController {

    private final CountryInfoService countryInfoService;

    // 국가 정보 조회
    @GetMapping("/country-info")
    public CountryInfoResponseDto getCountryInfo(
            @RequestParam String countryName
    ) {
        return countryInfoService.getCountryInfo(countryName);
    }

}