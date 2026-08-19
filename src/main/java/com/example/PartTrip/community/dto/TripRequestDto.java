package com.example.PartTrip.community.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TripRequestDto {

    private String title;
    private Long countryInfoId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String content;
    private List<String> images;
    private List<TripPlaceRequestDto> places;
}
