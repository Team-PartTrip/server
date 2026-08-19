package com.example.PartTrip.main.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecentSearchResponseDto {

    private Long recentSearchId;

    private String countryName;

    private String cityName;

    private String imageUrl;
}