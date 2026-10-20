package com.example.PartTrip.planner.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/** 플래너를 만들 때 그룹장이 정하는 도시 한 곳과 머무는 기간 */
@Getter
@Setter
public class PlannerCityRequestDto {

    @NotBlank(message = "시·도를 선택해주세요.")
    private String regionCode;

    @NotBlank(message = "도시를 입력해주세요.")
    private String cityName;

    @NotNull(message = "도시별 시작일을 입력해주세요.")
    private LocalDate startDate;

    @NotNull(message = "도시별 종료일을 입력해주세요.")
    private LocalDate endDate;
}
