package com.example.PartTrip.planner.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteConfirmRequestDto {

    // 단독 최다 득표 후보라면 생략 가능하다. 동점이면 공동 1위 후보를 지정한다
    private Long optionId;
}
