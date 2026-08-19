package com.example.PartTrip.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private String content;
    private Long parentCommentId;
}
