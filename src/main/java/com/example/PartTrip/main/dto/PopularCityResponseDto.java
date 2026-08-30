package com.example.PartTrip.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 플래너 만들기 첫 화면(C3)의 "인기 여행지".
// 이모지는 서버가 들고 있지 않다. 앱이 도시 이름으로 붙인다.
@Getter
@AllArgsConstructor
public class PopularCityResponseDto {

    private String cityName;
    private String countryName;
    /** 이 도시로 만들어진 여행 계획 수. 정렬 근거를 화면에서도 쓸 수 있게 같이 준다 */
    private Long planCount;
}
