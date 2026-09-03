package com.example.PartTrip.planner.dto.request;

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

    // API-008-06 호환: 투표 생성과 동시에 첫 장소 후보를 등록할 때 사용한다
    private Long placeId;

    // null이면 마감 시간이 없는 투표로 생성한다
    private LocalDateTime deadline;
}
