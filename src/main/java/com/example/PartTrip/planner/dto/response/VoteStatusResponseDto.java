package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class VoteStatusResponseDto {

    private Long voteId;
    private Long plannerId;
    private String category;
    private String categoryLabel;
    private String status;
    private LocalDateTime deadline;
    private Boolean deadlinePassed;
    private Long eligibleMemberCount;
    private Long votedMemberCount;
    private Long confirmedOptionId;
    private List<VoteOptionStatusResponseDto> options;
}
