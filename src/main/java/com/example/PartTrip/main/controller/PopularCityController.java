package com.example.PartTrip.main.controller;

import com.example.PartTrip.main.dto.PopularCityResponseDto;
import com.example.PartTrip.main.service.PopularCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class PopularCityController {

    private final PopularCityService popularCityService;

    // 인기 여행지 - 플래너 만들기 첫 화면(C3). 여행 계획이 많은 도시 순이다.
    @GetMapping("/popular-cities")
    public List<PopularCityResponseDto> getPopularCities(
            @RequestParam(required = false) Integer limit
    ) {
        return popularCityService.getPopularCities(limit);
    }
}
