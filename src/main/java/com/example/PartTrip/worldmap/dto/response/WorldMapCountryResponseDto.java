package com.example.PartTrip.worldmap.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WorldMapCountryResponseDto {
    private List<String> cities;
    private String countryName;
    private List<WorldMapTripSummaryDto> trips;
    private Integer visitCount;
}
