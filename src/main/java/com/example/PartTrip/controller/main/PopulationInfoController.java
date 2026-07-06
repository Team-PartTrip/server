package com.example.PartTrip.controller.main;

import com.example.PartTrip.dto.main.PopulationInfoResponseDto;
import com.example.PartTrip.service.main.PopulationInfoService;
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