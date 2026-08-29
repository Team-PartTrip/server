package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoteReminderResponseDto {
    private int notifiedCount;
    private String message;
}
