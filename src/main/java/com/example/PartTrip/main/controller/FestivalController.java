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
    @GetMapping("/festivals")
    public List<FestivalResponseDto> getFestivals(
            @RequestParam String countryName
    ) {
        return festivalService.getFestivals(countryName);
    }

}