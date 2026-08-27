package com.example.PartTrip.worldmap.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AcquireCountryRequestDto {

    @NotNull(message = "tripId는 필수입니다.")
    private Long tripId;
}
