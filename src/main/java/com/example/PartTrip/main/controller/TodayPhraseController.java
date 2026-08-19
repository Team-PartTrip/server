package com.example.PartTrip.main.controller;

import com.example.PartTrip.main.dto.TodayPhraseResponseDto;
import com.example.PartTrip.main.service.TodayPhraseService;
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