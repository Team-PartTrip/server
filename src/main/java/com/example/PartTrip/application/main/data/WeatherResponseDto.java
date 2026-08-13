package com.example.PartTrip.application.main.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeatherResponseDto {

    // 현재 기온 (섭씨)
    private Double temperature;

    // 체감 온도 (섭씨)
    private Double feelsLike;

    // 날씨 상태 한글 설명 (예: 맑음, 흐림, 비)
    private String description;
}
