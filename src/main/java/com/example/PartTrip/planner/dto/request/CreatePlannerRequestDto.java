package com.example.PartTrip.planner.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreatePlannerRequestDto {

    // 여행 제목
    @NotBlank(message = "여행 제목을 입력해주세요.")
    private String title;

    // 여행 인원 (본인 포함)
    @NotNull(message = "여행 인원을 입력해주세요.")
    @Min(value = 1, message = "여행 인원은 1명 이상이어야 합니다.")
    @Max(value = 30, message = "여행 인원은 30명을 넘을 수 없습니다.")
    private Integer memberCount;

    // 혼자인지 확인
    @NotNull(message = "혼자 여행인지 선택해주세요.")
    private Boolean isSolo;

    private String countryName;

    private String cityName;

    private LocalDate startDate;

    private LocalDate endDate;
}
