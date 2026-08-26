package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class VoteCreateResponseDto {

    private Long voteId;
    private Long plannerId;
    private Long planId;
    private String category;
    private String categoryLabel;
    private String status;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
}
