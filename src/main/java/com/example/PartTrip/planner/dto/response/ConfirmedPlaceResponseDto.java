package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConfirmedPlaceResponseDto {

    private Long voteId;
    private String category;
    private String categoryLabel;
    private Long optionId;
    private Long tourPlaceId;
    private String placeName;
    private String imageUrl;
    private String address;
    private Double rating;
    private Long voteCount;
}
