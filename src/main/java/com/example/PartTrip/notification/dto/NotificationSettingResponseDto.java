package com.example.PartTrip.notification.dto;

import com.example.PartTrip.notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationSettingResponseDto {

    private NotificationType type;

    private String label;

    private boolean enabled;
}
