package com.example.PartTrip.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardResponseDto {
    private Long boardId;
    private String userId;
    private String nickName;
    private String title;
    private String content;
    private Integer likeCount;
    private Long commentCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
