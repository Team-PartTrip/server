package com.example.PartTrip.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TourPlaceResponseDto {

    private Long tourPlaceId;

    private String placeName;

    // 카테고리 한글 이름 (맛집 / 명소 / 숙소 / 카페 / 액티비티 / 쇼핑)
    private String category;

    private String description;

    private String address;

    private Double rating;

    private String imageUrl;

    private Double latitude;

    private Double longitude;

}
