package com.example.PartTrip.notification.service;

import com.example.PartTrip.notification.dto.NotificationPageResponseDto;
import com.example.PartTrip.notification.dto.NotificationResponseDto;
import com.example.PartTrip.notification.entity.NotificationEntity;
import com.example.PartTrip.notification.enums.NotificationCategory;
import com.example.PartTrip.notification.enums.NotificationType;
import com.example.PartTrip.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private static final int MAX_PAGE_SIZE = 50;
    private static final int DEFAULT_PAGE_SIZE = 20;

    @Transactional(readOnly = true)
    public NotificationPageResponseDto getNotifications(
            String userId, String category, Long cursor, Integer size) {

        int pageSize = normalizeSize(size);
        NotificationCategory filter = parseCategory(category);

        // 다음 페이지가 있는지 알기 위해 한 건 더 가져온다. COUNT 를 따로 날리는 것보다 싸다.
        Pageable pageable = PageRequest.of(0, pageSize + 1);

        List<NotificationEntity> rows = findRows(userId, filter, cursor, pageable);

        boolean hasNext = rows.size() > pageSize;
        if (hasNext) {
            rows = rows.subList(0, pageSize);
        }

        List<NotificationResponseDto> items = rows.stream()
                .map(this::toDto)
                .toList();

        Long nextCursor = hasNext ? rows.get(rows.size() - 1).getNotificationId() : null;

        return new NotificationPageResponseDto(items, nextCursor, hasNext);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(String userId) {

        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markAsRead(String userId, Long notificationId) {

        NotificationEntity notification = notificationRepository
                .findByNotificationIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));

        // 이미 읽은 알림을 다시 눌러도 readAt 이 바뀌지 않도록 한다
        if (notification.isRead()) {
            return;
        }

        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
    }

    @Transactional
    public int markAllAsRead(String userId) {

        return notificationRepository.markAllAsRead(userId, LocalDateTime.now());
    }

    private List<NotificationEntity> findRows(
            String userId, NotificationCategory filter, Long cursor, Pageable pageable) {

        if (filter == null) {
            return cursor == null
                    ? notificationRepository
                        .findByUserIdOrderByNotificationIdDesc(userId, pageable)
                    : notificationRepository
                        .findByUserIdAndNotificationIdLessThanOrderByNotificationIdDesc(
                                userId, cursor, pageable);
        }

        return cursor == null
                ? notificationRepository
                    .findByUserIdAndCategoryOrderByNotificationIdDesc(userId, filter, pageable)
                : notificationRepository
                    .findByUserIdAndCategoryAndNotificationIdLessThanOrderByNotificationIdDesc(
                            userId, filter, cursor, pageable);
    }

    private NotificationCategory parseCategory(String category) {

        if (category == null || category.isBlank() || "ALL".equalsIgnoreCase(category)) {
            return null;
        }

        try {
            return NotificationCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("알 수 없는 알림 카테고리입니다: " + category);
        }
    }

    private int normalizeSize(Integer size) {

        if (size == null || size < 1) {
            return DEFAULT_PAGE_SIZE;
        }

        return Math.min(size, MAX_PAGE_SIZE);
    }

    private NotificationResponseDto toDto(NotificationEntity n) {

        return new NotificationResponseDto(
                n.getNotificationId(),
                n.getType(),
                n.getCategory(),
                n.getTitle(),
                n.getBody(),
                n.getLinkType(),
                n.getLinkId(),
                n.isRead(),
                n.getCreatedAt()
        );
    }
}
