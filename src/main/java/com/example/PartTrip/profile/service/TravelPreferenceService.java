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

    @Transactional
    public TravelPreferenceResponseDto updatePreference(
            String userId,
            TravelPreferenceRequestDto request
    ) {
        verifyUser(userId);
        TravelPreferenceEntity preference = travelPreferenceRepository.findById(userId)
                .orElseGet(() -> newPreference(userId));
        preference.setPreferredTransport(request.getPreferredTransport());
        preference.setDailyScheduleCount(request.getDailyScheduleCount());
        preference.setCanUseStairs(request.getCanUseStairs());
        return TravelPreferenceResponseDto.from(travelPreferenceRepository.save(preference));
    }

    private TravelPreferenceEntity newPreference(String userId) {
        TravelPreferenceEntity preference = new TravelPreferenceEntity();
        preference.setUserId(userId);
        return preference;
    }

    private void verifyUser(String userId) {
        if (!userProfileRepository.existsById(userId)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
    }
}
