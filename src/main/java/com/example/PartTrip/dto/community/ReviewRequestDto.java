package com.example.PartTrip.dto.community;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {

    private Long countryInfoId;
    private String title;
    private Integer rating;
    private String content;
}
