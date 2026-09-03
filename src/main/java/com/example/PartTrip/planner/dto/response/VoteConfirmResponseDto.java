package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoteConfirmResponseDto {

    private Long voteId;
    private String voteStatus;
    private Long confirmedOptionId;
    private Long tourPlaceId;
    private String placeName;
    private Long voteCount;
    private String plannerStatus;
}
