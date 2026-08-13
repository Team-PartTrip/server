package com.example.PartTrip.presentation.main;

import com.example.PartTrip.application.main.data.PopulationInfoResponseDto;
import com.example.PartTrip.application.main.PopulationInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class PopulationInfoController {

    private final PopulationInfoService populationInfoService;

    // 인구 구성 조회
    @GetMapping("/population-info")
    public List<PopulationInfoResponseDto> getPopulationInfo(
            @RequestParam String countryName
    ) {
        return populationInfoService.getPopulationInfo(countryName);
    }

}