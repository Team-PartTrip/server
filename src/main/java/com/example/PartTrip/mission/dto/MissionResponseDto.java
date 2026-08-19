package com.example.PartTrip.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MissionResponseDto {

    private Long missionId;

    private String missionTitle;

    private String missionDescription;

    private boolean completed;

    private String missionCountry;

    private String missionCategory;

    private Integer missionPoint;

    private String imgUrl;

}