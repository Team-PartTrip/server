package com.example.PartTrip.worldmap.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VisitedCountryResponseDto {
    private String countryCode;
    private String countryName;
}
