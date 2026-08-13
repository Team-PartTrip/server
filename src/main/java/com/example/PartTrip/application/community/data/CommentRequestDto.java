package com.example.PartTrip.application.community.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private String content;
    private Long parentCommentId;
}
