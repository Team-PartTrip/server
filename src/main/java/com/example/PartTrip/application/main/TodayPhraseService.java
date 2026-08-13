package com.example.PartTrip.application.main;

import com.example.PartTrip.application.main.data.TodayPhraseResponseDto;
import com.example.PartTrip.domain.main.entity.TodayPhraseEntity;
import com.example.PartTrip.domain.main.repository.TodayPhraseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodayPhraseService {

    private final TodayPhraseRepository todayPhraseRepository;

    // 오늘의 한마디 조회
    public TodayPhraseResponseDto getTodayPhrase(String countryName, Integer dayNumber) {

        TodayPhraseEntity phrase = todayPhraseRepository
                .findByCountryNameAndDayNumber(countryName, dayNumber)
                .orElseThrow(() -> new IllegalArgumentException("오늘의 한마디 정보가 없습니다."));

        return new TodayPhraseResponseDto(
                phrase.getDayNumber(),
                phrase.getPhrase(),
                phrase.getMeaning()
        );
    }
}