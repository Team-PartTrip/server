package com.example.PartTrip.planner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinPlannerRequestDto {

    @NotBlank(message = "초대 코드를 입력해주세요.")
    @Size(max = 20, message = "초대 코드는 20자를 넘을 수 없습니다.")
    private String inviteCode;
}
