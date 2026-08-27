package com.example.PartTrip.worldmap.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CountryTravelHistoryResponseDto {
    private String countryCode;
    private String countryName;
    private int visitCount;
    private List<String> cities;
    private List<TripResponseDto> trips;

    @Getter
    @Builder
    public static class TripResponseDto {
        private Long tripId;
        private String cityName;
        private LocalDate startDate;
        private LocalDate endDate;
    }
}
