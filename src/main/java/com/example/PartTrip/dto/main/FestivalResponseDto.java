package com.example.PartTrip.dto.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FestivalResponseDto {

    private Long festivalId;
    private String title;
    private String category;
    private String description;
    private String startDate;
    private String startTime;
    private String location;
    private String imageUrl;
}