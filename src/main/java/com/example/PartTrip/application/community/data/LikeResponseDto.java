package com.example.PartTrip.application.community.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponseDto {
    private boolean liked;
    private long likeCount;
}
