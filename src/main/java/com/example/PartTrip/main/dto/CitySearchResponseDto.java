package com.example.PartTrip.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 도시 검색 결과 한 줄 */
@Getter
@AllArgsConstructor
public class CitySearchResponseDto {

    // 도시 이름 (한글)
    private String cityName;

    // 도시가 속한 나라 이름 (한글)
    private String countryName;

    // 시·도 이름. 예) 경상북도. 구글이 주지 않으면 null
    private String regionName;
}
