package com.example.PartTrip.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NotificationPageResponseDto {

    private List<NotificationResponseDto> items;

    // 다음 페이지 요청에 그대로 넣어 보낼 값. 더 없으면 null.
    private Long nextCursor;

    private boolean hasNext;
}
