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
    @GetMapping("/tour-place")
    public List<TourPlaceResponseDto> getTourPlace(
            @RequestParam String countryName
    ) {
        return tourPlaceService.getTourPlace(countryName);
    }
}