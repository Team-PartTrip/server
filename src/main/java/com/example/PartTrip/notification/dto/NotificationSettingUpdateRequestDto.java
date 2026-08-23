package com.example.PartTrip.notification.dto;

import com.example.PartTrip.notification.enums.NotificationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 바뀐 토글만 보내도 되고 6개를 전부 보내도 된다. 보내지 않은 유형은 그대로 둔다.
@Getter
@Setter
public class NotificationSettingUpdateRequestDto {

    @NotEmpty(message = "변경할 알림 설정이 없습니다.")
    private List<@Valid Item> settings;

    @Getter
    @Setter
    public static class Item {

        @NotNull(message = "알림 종류를 지정해주세요.")
        private NotificationType type;

        @NotNull(message = "수신 여부를 지정해주세요.")
        private Boolean enabled;
    }
}
