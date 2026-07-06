package com.example.PartTrip.dto.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodayPhraseResponseDto {

    private Integer dayNumber;

    private String phrase;

    private String meaning;
}