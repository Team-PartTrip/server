package com.example.PartTrip.planner.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvitePlannerMembersRequestDto {

    @NotNull(message = "초대할 사용자 목록을 입력해주세요.")
    @Size(max = 30, message = "한 번에 30명보다 많이 초대할 수 없습니다.")
    private List<String> userIds;
}
