package com.example.PartTrip.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopulationInfoResponseDto {

    private Long populationInfoId;
    private String nationCode;
    private String nationName;
    private Integer percent;
}