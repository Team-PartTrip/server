package com.example.PartTrip.main.controller;

import com.example.PartTrip.main.dto.TourPlaceResponseDto;
import com.example.PartTrip.main.service.TourPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class TourPlaceController {

    private final TourPlaceService tourPlaceService;

    // 관광지 조회
    // 예) /api/main/tour-place?countryName=일본&cityName=오사카&category=맛집
    @GetMapping("/tour-place")
    public List<TourPlaceResponseDto> getTourPlace(
            @RequestParam String countryName,
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false) String category
    ) {
        return tourPlaceService.getTourPlace(countryName, cityName, category);
    }
}
