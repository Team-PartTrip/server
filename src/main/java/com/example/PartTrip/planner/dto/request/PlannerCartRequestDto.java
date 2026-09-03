package com.example.PartTrip.planner.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlannerCartRequestDto {

    @NotEmpty(message = "장바구니에 담을 장소를 선택해주세요.")
    private List<Long> placeIds;
}
