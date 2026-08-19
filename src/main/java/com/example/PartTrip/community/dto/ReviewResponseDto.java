package com.example.PartTrip.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<String> images;
    private long likeCount;
    private boolean liked;
    private long commentCount;
    private LocalDateTime createDate;
}
