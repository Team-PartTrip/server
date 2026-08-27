package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PlannerConfirmResponseDto {

    private Long plannerId;
    private List<ConfirmedPlaceResponseDto> confirmedSchedule;
    private Long tripCardId;
}
