package com.example.PartTrip.planner.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class VoteOptionResponseDto {

    private Long optionId;
    private Long voteId;
    private Long tourPlaceId;
    private String placeName;
    private String addedByUserId;
    private LocalDateTime createdAt;
}
