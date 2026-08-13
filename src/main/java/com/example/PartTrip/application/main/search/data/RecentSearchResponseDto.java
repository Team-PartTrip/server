package com.example.PartTrip.application.main.search.data;

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