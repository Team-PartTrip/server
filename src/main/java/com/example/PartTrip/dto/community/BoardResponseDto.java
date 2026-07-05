package com.example.PartTrip.dto.community;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class BoardResponseDto {
    private Long boardId;
    private String userId;
    private String nickName;
    private String title;
    private String content;
    private List<String> images;
    private long likeCount;
    private boolean liked;
    private long commentCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
