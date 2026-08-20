package com.example.PartTrip.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponseDto {
    private boolean liked;
    private long likeCount;
}
