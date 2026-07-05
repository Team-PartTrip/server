package com.example.PartTrip.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {

    private Long reviewId;
    private Long countryInfoId;
    private String countryName;
    private String cityName;
    private String userId;
    private String nickName;
    private String title;
    private Integer rating;
    private String content;
    private LocalDateTime createDate;
}
