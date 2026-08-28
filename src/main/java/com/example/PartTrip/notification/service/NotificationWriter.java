package com.example.PartTrip.notification.service;

import com.example.PartTrip.notification.entity.NotificationEntity;
import com.example.PartTrip.notification.enums.NotificationType;
import com.example.PartTrip.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

// 알림을 실제로 만들어 저장한다. 리스너와 스케줄러가 쓴다.
//
// 조회용 NotificationService 와 나눈 이유는 트랜잭션 경계가 다르기 때문이다.
// 이쪽은 AFTER_COMMIT 시점, 즉 원래 트랜잭션이 이미 끝난 뒤에 호출된다.
@Service
@RequiredArgsConstructor
public class NotificationWriter {

    private final NotificationRepository notificationRepository;

    // REQUIRES_NEW 로 새 트랜잭션을 연다.
    // AFTER_COMMIT 은 원래 트랜잭션이 끝나는 중에 실행되므로, 기본 전파 수준으로 두면
    // 이미 완료 단계에 들어간 트랜잭션에 합류해 저장이 커밋되지 않는다.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void write(String userId, NotificationType type,
                      String title, String body, String linkType, Long linkId) {

        writeAll(List.of(userId), type, title, body, linkType, linkId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void writeAll(Collection<String> userIds, NotificationType type,
                         String title, String body, String linkType, Long linkId) {

        List<NotificationEntity> toSave = userIds.stream()
                .distinct()
                .map(userId -> build(userId, type, title, body, linkType, linkId))
                .toList();

        if (toSave.isEmpty()) {
            return;
        }

        notificationRepository.saveAll(toSave);
    }

    private NotificationEntity build(String userId, NotificationType type,
                                     String title, String body, String linkType, Long linkId) {

        NotificationEntity notification = new NotificationEntity();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setCategory(type.getCategory());
        notification.setTitle(title);
        notification.setBody(body);
        notification.setLinkType(linkType);
        notification.setLinkId(linkId);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notification;
    }
}
