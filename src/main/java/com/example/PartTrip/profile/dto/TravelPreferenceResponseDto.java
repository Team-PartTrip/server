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

    public static TravelPreferenceResponseDto from(TravelPreferenceEntity preference) {
        return new TravelPreferenceResponseDto(
                preference.getPreferredTransport(),
                preference.getDailyScheduleCount(),
                preference.getCanUseStairs()
        );
    }
}
