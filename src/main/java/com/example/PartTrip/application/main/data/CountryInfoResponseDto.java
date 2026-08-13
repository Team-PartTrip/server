package com.example.PartTrip.application.main.data;

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
