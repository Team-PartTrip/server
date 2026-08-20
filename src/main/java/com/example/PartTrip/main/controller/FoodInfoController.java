package com.example.PartTrip.main.controller;

import com.example.PartTrip.main.dto.FoodInfoResponseDto;
import com.example.PartTrip.main.service.FoodInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class FoodInfoController {

    private final FoodInfoService foodInfoService;

    // 대표 음식 조회
    @GetMapping("/food-info")
    public List<FoodInfoResponseDto> getFoodInfo(
            @RequestParam String countryName
    ) {
        return foodInfoService.getFoodInfo(countryName);
    }
}