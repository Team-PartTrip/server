package com.example.PartTrip.planner.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SavePlannerTravelPlanRequestDto {

    @Min(value = 1, message = "여행 인원은 1명 이상이어야 합니다.")
    @Max(value = 30, message = "여행 인원은 30명을 넘을 수 없습니다.")
    private Integer memberCount;

    private Boolean isSolo;

    @NotBlank(message = "여행 국가를 입력해주세요.")
    @Size(max = 100, message = "국가 이름은 100자를 넘을 수 없습니다.")
    private String countryName;

    @NotBlank(message = "여행 도시를 입력해주세요.")
    @Size(max = 100, message = "도시 이름은 100자를 넘을 수 없습니다.")
    private String cityName;

    @NotNull(message = "여행 시작일을 입력해주세요.")
    private LocalDate startDate;

    @NotNull(message = "여행 종료일을 입력해주세요.")
    private LocalDate endDate;
}
