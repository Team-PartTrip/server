package com.example.PartTrip.dto.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DdayResponseDto {

    private String countryName;

    private String cityName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String dday;
}