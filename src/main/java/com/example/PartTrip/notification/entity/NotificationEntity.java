package com.example.PartTrip.notification.entity;

import com.example.PartTrip.notification.enums.NotificationCategory;
import com.example.PartTrip.notification.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 알림 (Func-004-01, Func-004-02)
@Entity
@Table(
        name = "notification",
        indexes = {
                // 목록은 항상 "내 알림을 최신순으로" 읽는다
                @Index(name = "idx_notification_user_created",
                        columnList = "user_id, created_at"),
                // 안읽음 배지 카운트 (앱 상단 "알림 3")
                @Index(name = "idx_notification_user_read",
                        columnList = "user_id, is_read")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    // 받는 사람
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 40)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private NotificationCategory category;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "body", length = 255)
    private String body;

    // 알림을 눌렀을 때 이동할 대상 (앱 E8 "투표 보러가기")
    @Column(name = "link_type", length = 30)
    private String linkType;

    @Column(name = "link_id")
    private Long linkId;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
