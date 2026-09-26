package com.example.PartTrip.region.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/** 대한민국 지도 — 내가 다녀온 시·도 (#162) */
@Getter
@Builder
public class RegionMapResponseDto {

    // 시·도 17곳. 지도가 "17곳 중 3곳" 을 그린다
    private long totalRegions;

    private List<VisitedRegionResponseDto> visited;

    private List<TripResponseDto> trips;

    @Getter
    @Builder
    public static class TripResponseDto {
        private Long tripCardId;
        private String regionCode;
        private String cityName;
        private List<double[]> points;
    }

    @Getter
    @Builder
    public static class VisitedRegionResponseDto {

        // 지도 GeoJSON 이 쓰는 행정표준코드 2자리
        private String regionCode;

        // 이름까지 내려준다. 이름이 바뀌어도 앱·웹을 배포하지 않으려면 서버가 줘야 한다
        private String regionName;

        private long tripCount;
    }
}
