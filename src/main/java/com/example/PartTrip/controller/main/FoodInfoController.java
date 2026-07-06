package com.example.PartTrip.controller.main;

import com.example.PartTrip.dto.main.FoodInfoResponseDto;
import com.example.PartTrip.service.main.FoodInfoService;
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