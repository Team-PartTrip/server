package com.example.PartTrip.notification.repository;

import com.example.PartTrip.notification.entity.NotificationEntity;
import com.example.PartTrip.notification.enums.NotificationCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    // Func-004-01 전체 탭
    List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    // Func-004-01 투표 / 기록 탭
    List<NotificationEntity> findByUserIdAndCategoryOrderByCreatedAtDesc(
            String userId, NotificationCategory category, Pageable pageable);

    // 읽음 처리 시 소유자까지 함께 확인한다
    Optional<NotificationEntity> findByNotificationIdAndUserId(Long notificationId, String userId);

    // 상단 배지 "알림 3"
    long countByUserIdAndIsReadFalse(String userId);

    // Func-004-02 "모두 읽음" 일괄 처리
    @Modifying
    @Query("""
            UPDATE NotificationEntity n
            SET n.isRead = true, n.readAt = :readAt
            WHERE n.userId = :userId AND n.isRead = false
            """)
    int markAllAsRead(String userId, LocalDateTime readAt);
}
