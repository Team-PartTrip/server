package com.example.PartTrip.controller.main;

import com.example.PartTrip.dto.main.TodayPhraseResponseDto;
import com.example.PartTrip.service.main.TodayPhraseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class TodayPhraseController {

    private final TodayPhraseService todayPhraseService;

    // 오늘의 한마디 조회
    @GetMapping("/today-phrase")
    public TodayPhraseResponseDto getTodayPhrase(
            @RequestParam String countryName,
            @RequestParam Integer dayNumber
    ) {
        return todayPhraseService.getTodayPhrase(countryName, dayNumber);
    }
}