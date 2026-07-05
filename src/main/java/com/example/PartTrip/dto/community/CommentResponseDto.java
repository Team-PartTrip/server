package com.example.PartTrip.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private Long boardId;
    private String userId;
    private String nickName;
    private String content;
    private LocalDateTime createDate;
}
