package com.example.PartTrip.notification.dto;

import com.example.PartTrip.notification.enums.NotificationCategory;
import com.example.PartTrip.notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationResponseDto {

    private Long notificationId;

    private NotificationType type;

    private NotificationCategory category;

    private String title;

    private String body;

    // 알림을 눌렀을 때 이동할 대상. 앱이 이 둘로 화면을 정한다.
    // 예) linkType "VOTE", linkId 12 → 투표 12번 화면
    private String linkType;

    private Long linkId;

    private boolean read;

    private LocalDateTime createdAt;
}
