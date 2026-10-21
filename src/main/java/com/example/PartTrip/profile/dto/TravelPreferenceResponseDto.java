package com.example.PartTrip.profile.dto;

import com.example.PartTrip.profile.entity.TravelPreferenceEntity;
import com.example.PartTrip.profile.enums.AvoidFood;
import com.example.PartTrip.profile.enums.PreferredTransport;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class TravelPreferenceResponseDto {

    private PreferredTransport preferredTransport;
    private Integer dailyScheduleCount;
    private Boolean canUseStairs;
    private Home home;
    private List<AvoidFood> avoidFoods;
    private Boolean usesWheelchair;
    private Boolean limitedWalking;
    private Integer maxWalkingMinutes;

    /** 이전 응답 계약을 사용하는 호출부의 호환 생성자. */
    public TravelPreferenceResponseDto(PreferredTransport preferredTransport,
                                       Integer dailyScheduleCount,
                                       Boolean canUseStairs,
                                       Home home,
                                       List<AvoidFood> avoidFoods,
                                       Boolean usesWheelchair,
                                       Boolean limitedWalking) {
        this(preferredTransport, dailyScheduleCount, canUseStairs, home, avoidFoods,
                usesWheelchair, limitedWalking,
                Boolean.TRUE.equals(limitedWalking) ? 30 : null);
    }

    public TravelPreferenceResponseDto(PreferredTransport preferredTransport,
                                       Integer dailyScheduleCount, Boolean canUseStairs) {
        this(preferredTransport, dailyScheduleCount, canUseStairs, null, null, null, null);
    }

    public record Home(String name, String address, double latitude, double longitude) {
    }

    /** 저장 엔티티를 외부 응답 형식으로 변환한다. */
    public static TravelPreferenceResponseDto from(TravelPreferenceEntity preference) {
        return new TravelPreferenceResponseDto(
                preference.getPreferredTransport(),
                preference.getDailyScheduleCount(),
                preference.getCanUseStairs(),
                preference.getHomeLatitude() == null || preference.getHomeLongitude() == null
                        ? null
                        : new Home(preference.getHomeName(), preference.getHomeAddress(),
                                preference.getHomeLatitude(), preference.getHomeLongitude()),
                avoidFoods(preference.getAvoidFoods()),
                preference.getUsesWheelchair(),
                preference.getLimitedWalking(),
                preference.getMaxWalkingMinutes() != null
                        ? preference.getMaxWalkingMinutes()
                        : Boolean.TRUE.equals(preference.getLimitedWalking()) ? 30 : null
        );
    }

    private static List<AvoidFood> avoidFoods(String joined) {
        if (joined == null) return null;
        return Arrays.stream(joined.split(","))
                .filter(name -> !name.isBlank())
                .map(AvoidFood::valueOf)
                .toList();
    }

    /** 구성원 설정을 보수적으로 합친다. 계단은 한 명이라도 어려우면 어려움, 음식은 합집합. */
    public static TravelPreferenceResponseDto aggregate(Collection<TravelPreferenceResponseDto> preferences) {
        List<TravelPreferenceResponseDto> values = preferences.stream().toList();
        if (values.isEmpty()) {
            return new TravelPreferenceResponseDto(
                    PreferredTransport.PUBLIC_TRANSIT, 3, true);
        }

        PreferredTransport transport = values.stream()
                .map(TravelPreferenceResponseDto::getPreferredTransport)
                .distinct().count() == 1
                ? values.get(0).getPreferredTransport() : PreferredTransport.PUBLIC_TRANSIT;
        int dailyCount = values.stream().map(TravelPreferenceResponseDto::getDailyScheduleCount)
                .filter(value -> value != null).min(Integer::compareTo).orElse(3);
        boolean canUseStairs = values.stream()
                .allMatch(value -> !Boolean.FALSE.equals(value.getCanUseStairs()));
        boolean wheelchair = values.stream()
                .anyMatch(value -> Boolean.TRUE.equals(value.getUsesWheelchair()));
        Integer walkingLimit = values.stream()
                .map(TravelPreferenceResponseDto::effectiveWalkingLimit)
                .filter(value -> value != null).min(Integer::compareTo).orElse(null);
        List<AvoidFood> avoid = values.stream()
                .map(TravelPreferenceResponseDto::getAvoidFoods)
                .filter(value -> value != null)
                .flatMap(Collection::stream)
                .distinct().toList();

        return new TravelPreferenceResponseDto(transport, dailyCount, canUseStairs, null,
                avoid, wheelchair, walkingLimit != null && walkingLimit < 60, walkingLimit);
    }

    private Integer effectiveWalkingLimit() {
        if (maxWalkingMinutes != null) return maxWalkingMinutes;
        return Boolean.TRUE.equals(limitedWalking) ? 30 : null;
    }

    public static Integer walkingLimit(TravelPreferenceResponseDto preference) {
        return preference == null ? null : preference.effectiveWalkingLimit();
    }
}
