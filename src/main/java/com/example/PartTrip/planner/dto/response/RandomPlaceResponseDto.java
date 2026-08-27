package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RandomPlaceResponseDto {

    private Long placeId;
    private String placeName;
}
