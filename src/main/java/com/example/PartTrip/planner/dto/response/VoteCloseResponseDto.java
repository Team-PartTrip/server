package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VoteCloseResponseDto {

    private Long voteId;
    private String status;
    private Long totalVoteCount;
    private Long highestVoteCount;
    private List<Long> topOptionIds;
    private Boolean tied;
}
