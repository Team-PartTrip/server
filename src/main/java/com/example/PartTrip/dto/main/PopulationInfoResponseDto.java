package com.example.PartTrip.dto.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopulationInfoResponseDto {

    private String nationCode;
    private String nationName;
    private Integer percent;
}