package com.example.PartTrip.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CountryInfoResponseDto {

    private Long countryInfoId;
    private String countryName;
    private String cityName;
    private String imageUrl;
    private String summary;
}
