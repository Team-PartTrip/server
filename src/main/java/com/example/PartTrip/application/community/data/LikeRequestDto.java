package com.example.PartTrip.application.community.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeRequestDto {
    // BOARD, REVIEW, TRIP
    private String targetType;
    private Long targetId;
}
