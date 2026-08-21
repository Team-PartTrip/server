package com.example.PartTrip.notification.repository;

import com.example.PartTrip.notification.entity.NotificationSettingEntity;
import com.example.PartTrip.notification.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationSettingRepository
        extends JpaRepository<NotificationSettingEntity, Long> {

    List<NotificationSettingEntity> findByUserId(String userId);

    Optional<NotificationSettingEntity> findByUserIdAndType(String userId, NotificationType type);

    // 설정 행이 없으면 수신으로 본다. 끈 경우만 false 로 존재한다.
    boolean existsByUserIdAndTypeAndEnabledFalse(String userId, NotificationType type);
}
