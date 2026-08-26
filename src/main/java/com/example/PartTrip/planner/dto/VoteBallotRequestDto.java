package com.example.PartTrip.planner.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteBallotRequestDto {

    @NotNull(message = "투표할 후보를 선택해주세요.")
    private Long optionId;
}
