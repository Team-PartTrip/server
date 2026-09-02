package com.example.PartTrip.worldmap.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WorldMapResponseDto {
    private long totalCountries;
    private List<VisitedCountryResponseDto> visited;
}
