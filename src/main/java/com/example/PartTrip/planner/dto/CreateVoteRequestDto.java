package com.example.PartTrip.planner.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateVoteRequestDto {

    // RESTAURANT 같은 enum 이름과 맛집 같은 한글 이름을 모두 허용한다
    @NotBlank(message = "투표 카테고리를 입력해주세요.")
    private String category;

    // null이면 마감 시간이 없는 투표로 생성한다
    private LocalDateTime deadline;
}
