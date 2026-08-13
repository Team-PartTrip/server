package com.example.PartTrip.application.community.data;

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
