package com.example.PartTrip.main.controller;

import com.example.PartTrip.main.dto.FestivalResponseDto;
import com.example.PartTrip.main.service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class FestivalController {

    private final FestivalService festivalService;

    // 축제 조회
    // year, month 를 생략하면 조회 시점의 연·월 기준으로 응답한다
    // 예) /api/main/festivals?countryName=일본&year=2026&month=8
    @GetMapping("/festivals")
    public List<FestivalResponseDto> getFestivals(
            @RequestParam String countryName,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        return festivalService.getFestivals(countryName, year, month);
    }

}
