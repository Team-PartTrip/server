package com.example.PartTrip.worldmap.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WorldMapVisitedCountryDto {
    private List<String> cities;
    private String countryCode;
    private String countryName;
    private Integer visitCount;
}
