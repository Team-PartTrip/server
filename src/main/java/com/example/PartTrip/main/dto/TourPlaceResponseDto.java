package com.example.PartTrip.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TourPlaceResponseDto {

    private String placeName;

    private String description;

    private String imageUrl;

    private Double latitude;

    private Double longitude;

}