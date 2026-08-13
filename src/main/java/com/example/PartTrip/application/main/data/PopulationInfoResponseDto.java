package com.example.PartTrip.application.main.data;

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