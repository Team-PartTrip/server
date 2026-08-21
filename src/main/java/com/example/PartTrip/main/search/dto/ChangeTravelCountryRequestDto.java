package com.example.PartTrip.main.search.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeTravelCountryRequestDto {

    @NotNull(message = "여행 계획을 선택해주세요.")
    private Long travelPlanId;

    @NotNull(message = "국가를 선택해주세요.")
    private Long countryInfoId;
}
