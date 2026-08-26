package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoteOptionStatusResponseDto {

    private Long optionId;
    private Long tourPlaceId;
    private String placeName;
    private String imageUrl;
    private String address;
    private Double rating;
    private String addedByUserId;
    private Long voteCount;
    private Boolean selectedByMe;
    private Boolean confirmed;
}
