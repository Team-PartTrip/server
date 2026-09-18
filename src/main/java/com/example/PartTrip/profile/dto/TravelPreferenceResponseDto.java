package com.example.PartTrip.profile.dto;

import com.example.PartTrip.profile.entity.TravelPreferenceEntity;
import com.example.PartTrip.profile.enums.PreferredTransport;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TravelPreferenceResponseDto {

    private PreferredTransport preferredTransport;
    private Integer dailyScheduleCount;
    private Boolean canUseStairs;

    /** 저장 엔티티를 외부 응답 형식으로 변환한다. */
    public static TravelPreferenceResponseDto from(TravelPreferenceEntity preference) {
        return new TravelPreferenceResponseDto(
                preference.getPreferredTransport(),
                preference.getDailyScheduleCount(),
                preference.getCanUseStairs()
        );
    }
}
