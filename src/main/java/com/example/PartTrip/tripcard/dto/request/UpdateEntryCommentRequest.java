package com.example.PartTrip.tripcard.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// 사진 코멘트 수정(API-003-08)의 요청 본문은 { comment } 다.
// 코멘트를 지우는 것도 수정이라 빈 값을 허용한다.
@Getter
@Setter
public class UpdateEntryCommentRequest {

    @Size(max = 100, message = "코멘트는 100자까지 쓸 수 있습니다.")
    private String comment;
}
