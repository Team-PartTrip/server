package com.example.PartTrip.dto.main.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopularPlaceResponseDto {

    private Long countryInfoId;

    private String countryName;

    private String cityName;

    private String imageUrl;
}