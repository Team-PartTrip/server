package com.example.PartTrip.planner.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvitePlannerMembersRequestDto {

    @NotNull(message = "초대할 사용자 목록을 입력해주세요.")
    private List<String> userIds;
}
