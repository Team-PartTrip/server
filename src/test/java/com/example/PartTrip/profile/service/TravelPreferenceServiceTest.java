package com.example.PartTrip.profile.service;

import com.example.PartTrip.profile.dto.TravelPreferenceRequestDto;
import com.example.PartTrip.profile.dto.TravelPreferenceResponseDto;
import com.example.PartTrip.profile.entity.TravelPreferenceEntity;
import com.example.PartTrip.profile.enums.AvoidFood;
import com.example.PartTrip.profile.enums.PreferredTransport;
import com.example.PartTrip.profile.repository.TravelPreferenceRepository;
import com.example.PartTrip.profile.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TravelPreferenceServiceTest {

    @Mock private TravelPreferenceRepository travelPreferenceRepository;
    @Mock private UserProfileRepository userProfileRepository;
    @InjectMocks private TravelPreferenceService travelPreferenceService;

    /** 설정이 없는 기존 사용자에게 약속된 기본값을 제공한다. */
    @Test
    void 저장된_설정이_없으면_기본값을_조회한다() {
        given(userProfileRepository.existsById("user")).willReturn(true);
        given(travelPreferenceRepository.findById("user")).willReturn(Optional.empty());

        TravelPreferenceResponseDto result = travelPreferenceService.getPreference("user");

        assertThat(result.getPreferredTransport()).isEqualTo(PreferredTransport.PUBLIC_TRANSIT);
        assertThat(result.getDailyScheduleCount()).isEqualTo(3);
        assertThat(result.getCanUseStairs()).isTrue();
    }

    /** 최초 수정 요청은 새 설정 행을 저장한다. */
    @Test
    void 이동수단_일정개수_계단여부를_저장한다() {
        given(userProfileRepository.findByUserIdForUpdate("user"))
                .willReturn(Optional.of(new com.example.PartTrip.signup.entity.UserEntity()));
        given(travelPreferenceRepository.findById("user")).willReturn(Optional.empty());
        given(travelPreferenceRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        TravelPreferenceRequestDto request = request(PreferredTransport.TAXI, 5, false);

        TravelPreferenceResponseDto result =
                travelPreferenceService.updatePreference("user", request);

        assertThat(result.getPreferredTransport()).isEqualTo(PreferredTransport.TAXI);
        assertThat(result.getDailyScheduleCount()).isEqualTo(5);
        assertThat(result.getCanUseStairs()).isFalse();
        verify(userProfileRepository).findByUserIdForUpdate("user");
    }

    /** 이미 저장된 설정은 같은 사용자 행에서 갱신한다. */
    @Test
    void 기존_설정을_수정한다() {
        TravelPreferenceEntity preference = new TravelPreferenceEntity();
        preference.setUserId("user");
        preference.setPreferredTransport(PreferredTransport.WALKING);
        preference.setDailyScheduleCount(2);
        preference.setCanUseStairs(true);
        given(userProfileRepository.findByUserIdForUpdate("user"))
                .willReturn(Optional.of(new com.example.PartTrip.signup.entity.UserEntity()));
        given(travelPreferenceRepository.findById("user")).willReturn(Optional.of(preference));
        given(travelPreferenceRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        TravelPreferenceResponseDto result = travelPreferenceService.updatePreference(
                "user", request(PreferredTransport.CAR, 4, false));

        assertThat(result.getPreferredTransport()).isEqualTo(PreferredTransport.CAR);
        assertThat(result.getDailyScheduleCount()).isEqualTo(4);
        assertThat(result.getCanUseStairs()).isFalse();
    }

    /** 가입하지 않은 사용자는 설정을 조회할 수 없다. */
    @Test
    void 피하는_음식을_저장하고_빼고_보내면_그대로_둔다() {
        TravelPreferenceEntity preference = new TravelPreferenceEntity();
        preference.setUserId("user");
        given(userProfileRepository.findByUserIdForUpdate("user"))
                .willReturn(Optional.of(new com.example.PartTrip.signup.entity.UserEntity()));
        given(travelPreferenceRepository.findById("user")).willReturn(Optional.of(preference));
        given(travelPreferenceRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        TravelPreferenceRequestDto request = request(PreferredTransport.PUBLIC_TRANSIT, 3, true);
        request.setAvoidFoods(List.of(AvoidFood.SEAFOOD, AvoidFood.SPICY, AvoidFood.SEAFOOD));

        assertThat(travelPreferenceService.updatePreference("user", request).getAvoidFoods())
                .containsExactly(AvoidFood.SEAFOOD, AvoidFood.SPICY);

        request.setAvoidFoods(null);
        assertThat(travelPreferenceService.updatePreference("user", request).getAvoidFoods())
                .containsExactly(AvoidFood.SEAFOOD, AvoidFood.SPICY);

        request.setAvoidFoods(List.of());
        assertThat(travelPreferenceService.updatePreference("user", request).getAvoidFoods())
                .isEmpty();
    }

    @Test
    void 휠체어와_오래_걷기를_저장하고_빼고_보내면_그대로_둔다() {
        TravelPreferenceEntity preference = new TravelPreferenceEntity();
        preference.setUserId("user");
        given(userProfileRepository.findByUserIdForUpdate("user"))
                .willReturn(Optional.of(new com.example.PartTrip.signup.entity.UserEntity()));
        given(travelPreferenceRepository.findById("user")).willReturn(Optional.of(preference));
        given(travelPreferenceRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        TravelPreferenceRequestDto request = request(PreferredTransport.PUBLIC_TRANSIT, 3, false);
        request.setUsesWheelchair(true);
        request.setLimitedWalking(false);

        TravelPreferenceResponseDto saved = travelPreferenceService.updatePreference("user", request);
        assertThat(saved.getUsesWheelchair()).isTrue();
        assertThat(saved.getLimitedWalking()).isFalse();

        request.setUsesWheelchair(null);
        request.setLimitedWalking(null);
        TravelPreferenceResponseDto kept = travelPreferenceService.updatePreference("user", request);
        assertThat(kept.getUsesWheelchair()).isTrue();
        assertThat(kept.getLimitedWalking()).isFalse();
    }

    @Test
    void 피하는_음식을_묻지_않았으면_null로_준다() {
        TravelPreferenceEntity preference = new TravelPreferenceEntity();
        preference.setUserId("user");
        given(userProfileRepository.existsById("user")).willReturn(true);
        given(travelPreferenceRepository.findById("user")).willReturn(Optional.of(preference));

        assertThat(travelPreferenceService.getPreference("user").getAvoidFoods()).isNull();
    }

    @Test
    void 존재하지_않는_사용자의_설정은_조회하지_않는다() {
        given(userProfileRepository.existsById("missing")).willReturn(false);

        assertThatThrownBy(() -> travelPreferenceService.getPreference("missing"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다");
    }

    /** 요청 DTO를 테스트 값으로 구성한다. */
    private TravelPreferenceRequestDto request(
            PreferredTransport transport,
            int dailyCount,
            boolean canUseStairs
    ) {
        TravelPreferenceRequestDto request = new TravelPreferenceRequestDto();
        request.setPreferredTransport(transport);
        request.setDailyScheduleCount(dailyCount);
        request.setCanUseStairs(canUseStairs);
        return request;
    }
}
