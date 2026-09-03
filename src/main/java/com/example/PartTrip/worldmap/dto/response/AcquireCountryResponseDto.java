package com.example.PartTrip.worldmap.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AcquireCountryResponseDto {
    private String countryCode;
    @Getter(onMethod_ = @JsonProperty("isNew"))
    private boolean isNew;
}
