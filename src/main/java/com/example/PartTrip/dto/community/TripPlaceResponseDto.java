package com.example.PartTrip.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TripPlaceResponseDto {

    private Long tripPlaceId;
    private Integer dayNumber;
    private String placeName;
    private String placeSub;
}
