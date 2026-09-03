package com.example.PartTrip.worldmap.dto.response;

import com.example.PartTrip.worldmap.enums.Continent;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class WorldMapStatsResponseDto {
    private long acquiredCount;
    private long totalCount;
    private BigDecimal percentage;
    private List<ContinentStatsResponseDto> byContinent;

    @Getter
    @Builder
    public static class ContinentStatsResponseDto {
        private Continent continent;
        private long acquiredCount;
        private long totalCount;
    }
}
