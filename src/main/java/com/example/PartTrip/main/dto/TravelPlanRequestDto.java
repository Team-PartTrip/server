package com.example.PartTrip.main.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    // 함께 가는 인원 수 (본인 포함)
    @Min(value = 1, message = "인원은 1명 이상이어야 합니다.")
    @Max(value = 30, message = "인원은 30명을 넘을 수 없습니다.")
    private Integer headcount;
}