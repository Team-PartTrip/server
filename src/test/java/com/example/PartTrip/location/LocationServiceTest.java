package com.example.PartTrip.location;

import com.example.PartTrip.global.exception.ForbiddenException;
import com.example.PartTrip.guardian.service.GuardianService;
import com.example.PartTrip.location.LocationDtos.UpdateRequest;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/** 위치 공유 (#160). 여행 중에만, 연결된 보호자에게만, 마지막 한 곳만 */
@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    private static final String SENIOR = "senior";
    private static final String CHILD = "child";

    @Mock private UserLocationRepository locationRepository;
    @Mock private GroupTravelPlanRepository groupTravelPlanRepository;
    @Mock private GuardianService guardianService;
    @InjectMocks private LocationService service;

    private void givenTraveling(boolean traveling) {
        LocalDate today = LocalDate.now();
        given(groupTravelPlanRepository.existsOverlappingPlanForUser(SENIOR, today, today)).willReturn(traveling);
    }

    private UserLocationEntity saved(double lat) {
        UserLocationEntity location = new UserLocationEntity();
        location.setUserId(SENIOR);
        location.setLatitude(lat);
        location.setLongitude(129.2);
        location.setRecordedAt(LocalDateTime.now());
        return location;
    }

    @Test
    void 여행_중이면_위치를_덮어쓴다() {
        givenTraveling(true);
        given(locationRepository.findById(SENIOR)).willReturn(Optional.of(saved(35.0)));

        service.update(SENIOR, new UpdateRequest(35.83, 129.21));

        // 새 줄을 만들지 않고 있던 줄을 바꾼다. 사용자마다 한 줄
        ArgumentCaptor<UserLocationEntity> captor = ArgumentCaptor.forClass(UserLocationEntity.class);
        verify(locationRepository).save(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(SENIOR);
        assertThat(captor.getValue().getLatitude()).isEqualTo(35.83);
    }

    @Test
    void 여행_기간이_아니면_저장하지_않는다() {
        givenTraveling(false);

        assertThatThrownBy(() -> service.update(SENIOR, new UpdateRequest(35.83, 129.21)))
                .isInstanceOf(IllegalArgumentException.class);
        verify(locationRepository, never()).save(any());
    }

    @Test
    void 연결된_보호자는_위치를_본다() {
        givenTraveling(true);
        given(locationRepository.findById(SENIOR)).willReturn(Optional.of(saved(35.83)));

        assertThat(service.getForGuardian(SENIOR, CHILD)).get()
                .extracting(LocationDtos.LocationResponse::latitude).isEqualTo(35.83);
    }

    @Test
    void 연결_안_된_사람은_위치를_못_본다() {
        willThrow(new ForbiddenException("연결된 보호자만 볼 수 있습니다."))
                .given(guardianService).requireLink(SENIOR, "stranger");

        assertThatThrownBy(() -> service.getForGuardian(SENIOR, "stranger"))
                .isInstanceOf(ForbiddenException.class);
        verify(locationRepository, never()).findById(any());
    }

    @Test
    void 여행이_끝났으면_남은_위치도_보여주지_않는다() {
        givenTraveling(false);

        assertThat(service.getForGuardian(SENIOR, CHILD)).isEmpty();
        verify(locationRepository, never()).findById(any());
    }

    @Test
    void 오래된_위치는_지운다() {
        service.deleteStale();

        ArgumentCaptor<LocalDateTime> before = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(locationRepository).deleteByRecordedAtBefore(before.capture());
        assertThat(before.getValue()).isBefore(LocalDateTime.now().minusHours(LocationService.KEEP_HOURS - 1));
    }
}
