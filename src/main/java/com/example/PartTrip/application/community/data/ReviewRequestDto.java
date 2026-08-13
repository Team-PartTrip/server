package com.example.PartTrip.application.community.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewRequestDto {

    private Long countryInfoId;
    private String title;
    private Integer rating;
    private String content;
    private List<String> images;
}
