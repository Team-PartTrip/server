package com.example.PartTrip.application.main.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TravelPlanRequestDto {

    private String countryName;

    private String cityName;

    private LocalDate startDate;

    private LocalDate endDate;
}