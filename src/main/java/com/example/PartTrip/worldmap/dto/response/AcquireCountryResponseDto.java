package com.example.PartTrip.worldmap.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AcquireCountryResponseDto {
    private String countryCode;
    private Boolean isNew;
}
