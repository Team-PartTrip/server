package com.example.PartTrip.application.community.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class TripResponseDto {

    private Long tripId;
    private String userId;
    private String nickName;
    private String title;
    private Long countryInfoId;
    private String countryName;
    private String cityName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String content;
    private List<String> images;
    private long likeCount;
    private boolean liked;
    private long commentCount;
    private Boolean isPublic;
    private LocalDateTime createDate;
    private List<TripPlaceResponseDto> places;
}
