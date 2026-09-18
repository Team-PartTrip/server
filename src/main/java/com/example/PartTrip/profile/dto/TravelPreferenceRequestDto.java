package com.example.PartTrip.profile.dto;

import com.example.PartTrip.profile.enums.PreferredTransport;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TravelPreferenceRequestDto {

    @NotNull(message = "선호 이동수단은 필수입니다.")
    private PreferredTransport preferredTransport;

    @NotNull(message = "하루 일정 개수는 필수입니다.")
    @Min(value = 1, message = "하루 일정 개수는 1개 이상이어야 합니다.")
    @Max(value = 10, message = "하루 일정 개수는 10개 이하여야 합니다.")
    private Integer dailyScheduleCount;

    @NotNull(message = "계단 이용 가능 여부는 필수입니다.")
    private Boolean canUseStairs;
}
