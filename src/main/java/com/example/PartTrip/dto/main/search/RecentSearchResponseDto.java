package com.example.PartTrip.dto.main.search;

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