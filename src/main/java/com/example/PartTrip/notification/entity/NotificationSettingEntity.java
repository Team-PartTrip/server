package com.example.PartTrip.notification.entity;

import com.example.PartTrip.notification.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 알림 유형별 수신 설정 (Func-004-03)
//
// 유형별 boolean 컬럼 대신 행 단위로 둔다.
// 알림 종류가 늘어도 DDL 이 필요 없다.
@Entity
@Table(
        name = "notification_setting",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_notification_setting_user_type",
                columnNames = {"user_id", "type"})
)
@Getter
@Setter
@NoArgsConstructor
public class NotificationSettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_setting_id")
    private Long notificationSettingId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 40)
    private NotificationType type;

    // 행이 없으면 수신으로 본다. 끈 것만 저장해도 되고 전부 저장해도 된다.
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
}
