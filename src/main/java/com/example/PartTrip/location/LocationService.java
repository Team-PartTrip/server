package com.example.PartTrip.location;

import com.example.PartTrip.guardian.service.GuardianService;
import com.example.PartTrip.location.LocationDtos.LocationResponse;
import com.example.PartTrip.location.LocationDtos.UpdateRequest;
import com.example.PartTrip.planner.repository.GroupTravelPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    /** 이보다 오래된 위치는 지운다. 여행 중이면 몇 분마다 새로 오므로 남아 있을 이유가 없다 */
    static final int KEEP_HOURS = 12;

    private final UserLocationRepository locationRepository;
    private final GroupTravelPlanRepository groupTravelPlanRepository;
    private final GuardianService guardianService;

    /** 내 위치 보내기. 여행 기간이 아니면 저장하지 않는다 */
    @Transactional
    public void update(String userId, UpdateRequest request) {
        if (!isTraveling(userId)) {
            throw new IllegalArgumentException("여행 중일 때만 위치를 공유해요.");
        }
        UserLocationEntity location = locationRepository.findById(userId).orElseGet(() -> {
            UserLocationEntity created = new UserLocationEntity();
            created.setUserId(userId);
            return created;
        });
        location.setLatitude(request.latitude());
        location.setLongitude(request.longitude());
        // 기기 시계는 믿지 않는다. 보호자 화면의 "○분 전" 이 틀어진다
        location.setRecordedAt(LocalDateTime.now());
        locationRepository.save(location);
    }

    /** 공유 그만하기. 지금 위치를 바로 지운다 */
    @Transactional
    public void stop(String userId) {
        locationRepository.deleteById(userId);
    }

    /** 보호자가 시니어 위치 보기. 여행 중이 아니거나 보낸 적이 없으면 비어 있다 */
    @Transactional(readOnly = true)
    public Optional<LocationResponse> getForGuardian(String seniorUserId, String guardianUserId) {
        guardianService.requireLink(seniorUserId, guardianUserId);
        if (!isTraveling(seniorUserId)) {
            return Optional.empty();
        }
        return locationRepository.findById(seniorUserId)
                .map(l -> new LocationResponse(l.getLatitude(), l.getLongitude(), l.getRecordedAt()));
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void deleteStale() {
        long deleted = locationRepository.deleteByRecordedAtBefore(LocalDateTime.now().minusHours(KEEP_HOURS));
        if (deleted > 0) {
            log.info("오래된 위치 {}건 삭제", deleted);
        }
    }

    /** 오늘이 여행 기간인 플래너가 있는지. 기간 겹침 쿼리를 오늘 하루로 부른다 */
    private boolean isTraveling(String userId) {
        LocalDate today = LocalDate.now();
        return groupTravelPlanRepository.existsOverlappingPlanForUser(userId, today, today);
    }
}
