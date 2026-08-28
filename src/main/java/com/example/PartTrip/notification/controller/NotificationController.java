package com.example.PartTrip.notification.controller;

import com.example.PartTrip.notification.dto.NotificationPageResponseDto;
import com.example.PartTrip.notification.dto.UnreadCountResponseDto;
import com.example.PartTrip.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // Func-004-01 알림 목록
    @GetMapping
    public NotificationPageResponseDto getNotifications(
            Authentication authentication,
            @RequestParam(defaultValue = "ALL") String category,
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false) Integer size) {

        String userId = (String) authentication.getPrincipal();

        return notificationService.getNotifications(userId, category, cursor, size);
    }

    // Func-004-01 상단 배지
    @GetMapping("/unread-count")
    public UnreadCountResponseDto getUnreadCount(Authentication authentication) {

        String userId = (String) authentication.getPrincipal();

        return new UnreadCountResponseDto(notificationService.getUnreadCount(userId));
    }

    // Func-004-02 읽음 처리
    @PatchMapping("/{notificationId}/read")
    public void markAsRead(
            Authentication authentication,
            @PathVariable Long notificationId) {

        String userId = (String) authentication.getPrincipal();

        notificationService.markAsRead(userId, notificationId);
    }

    // Func-004-02 모두 읽음
    @PatchMapping("/read-all")
    public UnreadCountResponseDto markAllAsRead(Authentication authentication) {

        String userId = (String) authentication.getPrincipal();

        notificationService.markAllAsRead(userId);

        // 배지를 다시 그릴 수 있도록 남은 개수를 돌려준다
        return new UnreadCountResponseDto(notificationService.getUnreadCount(userId));
    }
}
