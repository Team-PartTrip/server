package com.example.PartTrip.worldmap.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WorldMapStatsResponseDto {
    private Integer acquiredCount;
    private List<ContinentProgressDto> byContinent;
    private Double percentage;
    private Integer totalCount;
}
