package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class VoteBallotResponseDto {

    private Long voteRecordId;
    private Long voteId;
    private Long optionId;
    private String placeName;
    private Boolean changed;
    private LocalDateTime votedAt;
}
