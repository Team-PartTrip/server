package com.example.PartTrip.dto.photo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbyPlaceRecommendationResponseDto {
    private String name;
    private String description;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private int distanceMeters;
    private String sourceName;
    private String sourceUrl;
}
