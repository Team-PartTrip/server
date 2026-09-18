package com.example.PartTrip.profile.service;

import com.example.PartTrip.profile.dto.TravelPreferenceRequestDto;
import com.example.PartTrip.profile.dto.TravelPreferenceResponseDto;
import com.example.PartTrip.profile.entity.TravelPreferenceEntity;
import com.example.PartTrip.profile.enums.PreferredTransport;
import com.example.PartTrip.profile.repository.TravelPreferenceRepository;
import com.example.PartTrip.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TravelPreferenceService {

    public static final PreferredTransport DEFAULT_TRANSPORT = PreferredTransport.PUBLIC_TRANSIT;
    public static final int DEFAULT_DAILY_SCHEDULE_COUNT = 3;
    public static final boolean DEFAULT_CAN_USE_STAIRS = true;

    private final TravelPreferenceRepository travelPreferenceRepository;
    private final UserProfileRepository userProfileRepository;

    /** 저장된 여행 편의 설정을 조회하고, 없으면 서비스 기본값을 반환한다. */
    @Transactional(readOnly = true)
    public TravelPreferenceResponseDto getPreference(String userId) {
        verifyUser(userId);
        return travelPreferenceRepository.findById(userId)
                .map(TravelPreferenceResponseDto::from)
                .orElseGet(() -> new TravelPreferenceResponseDto(
                        DEFAULT_TRANSPORT,
                        DEFAULT_DAILY_SCHEDULE_COUNT,
                        DEFAULT_CAN_USE_STAIRS
                ));
    }

    /** 사용자별 여행 편의 설정을 신규 저장하거나 기존 값을 수정한다. */
    @Transactional
    public TravelPreferenceResponseDto updatePreference(
            String userId,
            TravelPreferenceRequestDto request
    ) {
        // 사용자 행을 잠가 같은 사용자의 최초 설정 요청이 동시에 들어와도
        // travel_preference PK(user_id)를 중복 생성하지 않도록 직렬화한다.
        userProfileRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        TravelPreferenceEntity preference = travelPreferenceRepository.findById(userId)
                .orElseGet(() -> newPreference(userId));
        preference.setPreferredTransport(request.getPreferredTransport());
        preference.setDailyScheduleCount(request.getDailyScheduleCount());
        preference.setCanUseStairs(request.getCanUseStairs());
        return TravelPreferenceResponseDto.from(travelPreferenceRepository.save(preference));
    }

    /** 신규 설정 행의 사용자 식별자를 초기화한다. */
    private TravelPreferenceEntity newPreference(String userId) {
        TravelPreferenceEntity preference = new TravelPreferenceEntity();
        preference.setUserId(userId);
        return preference;
    }

    /** 설정 조회 전에 실제 가입 사용자인지 확인한다. */
    private void verifyUser(String userId) {
        if (!userProfileRepository.existsById(userId)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }
}
