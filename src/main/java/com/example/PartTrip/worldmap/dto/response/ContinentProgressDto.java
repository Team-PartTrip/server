package com.example.PartTrip.worldmap.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContinentProgressDto {
    private String continent;
    private Integer acquiredCount;
    private Integer totalCount;
}
