package com.example.PartTrip.application.main.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodayPhraseResponseDto {

    private Integer dayNumber;

    private String phrase;

    private String meaning;
}