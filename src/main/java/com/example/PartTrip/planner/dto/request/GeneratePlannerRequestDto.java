package com.example.PartTrip.planner.dto.request;

import com.example.PartTrip.planner.enums.PlannerBlockType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** 블록 지침으로 AI 일정 초안 만들기 (#158). 국내 여행만 받는다 */
@Getter
@Setter
public class GeneratePlannerRequestDto {

    @NotBlank(message = "여행 제목을 입력해주세요.")
    @Size(max = 100, message = "여행 제목은 100자를 넘을 수 없습니다.")
    private String title;

    @NotNull(message = "여행 인원을 입력해주세요.")
    @Min(value = 1, message = "여행 인원은 1명 이상이어야 합니다.")
    @Max(value = 30, message = "여행 인원은 30명을 넘을 수 없습니다.")
    private Integer memberCount;

    @NotNull(message = "혼자 여행인지 선택해주세요.")
    private Boolean isSolo;

    /** 국내 도시 · 지역 이름. 예) 강릉, 경주 */
    @NotBlank(message = "여행할 지역을 골라주세요.")
    @Size(max = 50, message = "지역 이름이 너무 깁니다.")
    private String cityName;

    @NotNull(message = "출발일을 골라주세요.")
    private LocalDate startDate;

    @NotNull(message = "종료일을 골라주세요.")
    private LocalDate endDate;

    @Valid
    @Size(max = 100, message = "블록은 100개까지 넣을 수 있습니다.")
    private List<Block> blocks = new ArrayList<>();

    @Getter
    @Setter
    public static class Block {

        @NotNull(message = "블록 종류가 비어 있습니다.")
        private PlannerBlockType type;

        @NotBlank(message = "블록 값이 비어 있습니다.")
        @Size(max = 100, message = "블록 값은 100자를 넘을 수 없습니다.")
        private String value;
    }
}
