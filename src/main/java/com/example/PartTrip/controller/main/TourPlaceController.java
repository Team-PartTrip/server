package com.example.PartTrip.controller.main;

import com.example.PartTrip.dto.main.TourPlaceResponseDto;
import com.example.PartTrip.service.main.TourPlaceService;
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