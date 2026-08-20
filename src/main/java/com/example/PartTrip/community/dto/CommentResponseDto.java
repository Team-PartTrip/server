package com.example.PartTrip.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String targetType;
    private Long targetId;
    private Long parentCommentId;
    private String userId;
    private String nickName;
    private String content;
    private LocalDateTime createDate;
}
