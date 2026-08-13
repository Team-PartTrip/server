package com.example.PartTrip.presentation.main;

import com.example.PartTrip.application.main.data.FestivalResponseDto;
import com.example.PartTrip.application.main.FestivalService;
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