package com.example.PartTrip.worldmap.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcquireCountryRequestDto {

    @NotNull(message = "여행 ID를 입력해주세요.")
    private Long tripId;
}
