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

    // 목록은 offset 이 아니라 notificationId 커서로 읽는다.
    // 알림은 보는 도중에도 쌓이므로 offset 을 쓰면 다음 페이지에서 항목이 밀려
    // 같은 알림이 두 번 나오거나 건너뛰어진다.
    // notificationId 는 IDENTITY 라 시간순으로 증가해 created_at 정렬과 결과가 같으면서,
    // created_at 이 같은 행이 있어도 경계가 흔들리지 않는다.

    List<NotificationEntity> findByUserIdOrderByNotificationIdDesc(
            String userId, Pageable pageable);

    List<NotificationEntity> findByUserIdAndNotificationIdLessThanOrderByNotificationIdDesc(
            String userId, Long cursor, Pageable pageable);

    List<NotificationEntity> findByUserIdAndCategoryOrderByNotificationIdDesc(
            String userId, NotificationCategory category, Pageable pageable);

    List<NotificationEntity> findByUserIdAndCategoryAndNotificationIdLessThanOrderByNotificationIdDesc(
            String userId, NotificationCategory category, Long cursor, Pageable pageable);

    // 소유자까지 함께 확인해 남의 알림에 접근할 수 없게 한다
    Optional<NotificationEntity> findByNotificationIdAndUserId(Long notificationId, String userId);

    long countByUserIdAndIsReadFalse(String userId);

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE NotificationEntity n
            SET n.isRead = true, n.readAt = :readAt
            WHERE n.userId = :userId AND n.isRead = false
            """)
    int markAllAsRead(String userId, LocalDateTime readAt);
}
