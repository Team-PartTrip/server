package com.example.PartTrip.notification.listener;

import com.example.PartTrip.notification.enums.NotificationType;
import com.example.PartTrip.notification.event.RegionVisitedEvent;
import com.example.PartTrip.notification.repository.NotificationRepository;
import com.example.PartTrip.notification.service.NotificationWriter;
import com.example.PartTrip.tripcard.repository.TripCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecordNotificationListenerTest {

    @Mock private NotificationWriter notificationWriter;
    @Mock private TripCardRepository tripCardRepository;
    @Mock private NotificationRepository notificationRepository;
    @InjectMocks private RecordNotificationListener listener;

    @Test
    void 처음_가는_시도면_알린다() {
        given(notificationRepository.existsByUserIdAndTypeAndLinkId(
                "traveler", NotificationType.REGION_VISITED, 51L)).willReturn(false);

        listener.on(new RegionVisitedEvent("51", "traveler"));

        verify(notificationWriter).write(
                eq("traveler"), eq(NotificationType.REGION_VISITED), anyString(),
                eq("강원특별자치도 방문이 지도에 기록됐어요."), eq("REGION_MAP"), eq(51L));
    }

    @Test
    void 이미_알린_시도면_다시_알리지_않는다() {
        given(notificationRepository.existsByUserIdAndTypeAndLinkId(
                "traveler", NotificationType.REGION_VISITED, 51L)).willReturn(true);

        listener.on(new RegionVisitedEvent("51", "traveler"));

        verify(notificationWriter, never()).write(
                anyString(), any(), anyString(), anyString(), anyString(), any());
    }
}
