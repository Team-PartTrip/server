package com.example.PartTrip.planner.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVoteOptionRequestDto {

    // 관광지 검색 결과를 후보로 등록할 때 사용한다
    private Long tourPlaceId;

    // 직접 입력 후보일 때 사용한다. tourPlaceId가 있으면 관광지 이름을 우선한다
    @Size(max = 255, message = "장소 이름은 255자를 넘을 수 없습니다.")
    private String placeName;
}
