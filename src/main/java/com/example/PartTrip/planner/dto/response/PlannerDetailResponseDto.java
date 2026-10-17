package com.example.PartTrip.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class PlannerDetailResponseDto {

    private Long plannerId;
    private String title;
    private String regionCode;
    private String regionName;
    private String cityName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String role;
    private Integer memberCount;
    private Long joinedMemberCount;
    private String inviteLink;

    /**
     * 도는 도시들. 도시를 하나만 고른 여행이면 한 줄이다.
     *
     * regionCode / cityName 은 첫 도시라 그대로 두었다. 도시 하나만 보던
     * 화면이 안 깨진다.
     */
    private List<PlannerCityResponseDto> cities;
}
