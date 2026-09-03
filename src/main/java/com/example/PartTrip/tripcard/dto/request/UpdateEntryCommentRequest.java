package com.example.PartTrip.tripcard.dto.request;

import lombok.Getter;
import lombok.Setter;

// 사진 코멘트 수정(API-003-08)의 요청 본문은 { comment } 다.
// 길이·공백 규칙은 추가 경로와 같아야 해서 서비스 한 곳에서 본다.
@Getter
@Setter
public class UpdateEntryCommentRequest {

    private String comment;
}
