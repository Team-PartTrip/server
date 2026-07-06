package com.example.PartTrip.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponseDto {
    private boolean liked;
    private long likeCount;
}
