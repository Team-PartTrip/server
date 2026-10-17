package com.example.PartTrip.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/** 장소 더 받기 (#139). 새로 받은 곳과, 다음에 그대로 보낼 cursor */
@Getter
@AllArgsConstructor
public class MoreTourPlacesResponseDto {

    private List<TourPlaceResponseDto> places;

    // 다음 번 요청에 그대로 보낸다. null 이면 구글이 더 줄 게 없다
    private String cursor;
}
