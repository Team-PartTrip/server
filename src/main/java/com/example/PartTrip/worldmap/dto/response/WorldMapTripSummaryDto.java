package com.example.PartTrip.worldmap.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class WorldMapTripSummaryDto {
    private String cityName;
    private LocalDate endDate;
    private LocalDate startDate;
    private Long tripId;
}
