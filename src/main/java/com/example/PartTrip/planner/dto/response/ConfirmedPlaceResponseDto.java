package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ConfirmedPlaceResponseDto {

    private String category;
    private String categoryLabel;
    private Long tourPlaceId;
    private String placeName;
    private String imageUrl;
    private String address;
    private Double rating;
    /** 며칠째 가는 곳인지. 확정 전 일정 생성기가 만드는 중간값에는 없다 */
    private LocalDate visitedDate;
}
