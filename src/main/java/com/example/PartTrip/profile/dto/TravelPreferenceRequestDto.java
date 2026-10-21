package com.example.PartTrip.profile.dto;

import com.example.PartTrip.profile.enums.AvoidFood;
import com.example.PartTrip.profile.enums.PreferredTransport;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

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

    private List<@NotNull AvoidFood> avoidFoods;

    private Boolean usesWheelchair;
    private Boolean limitedWalking;

    /** 한 번에 걸을 수 있는 시간(분). 60은 1시간 이상을 뜻한다. */
    @Min(value = 10, message = "걸을 수 있는 시간은 10분, 30분, 60분 중 하나여야 합니다.")
    @Max(value = 60, message = "걸을 수 있는 시간은 10분, 30분, 60분 중 하나여야 합니다.")
    private Integer maxWalkingMinutes;

    @jakarta.validation.constraints.AssertTrue(message = "걸을 수 있는 시간은 10분, 30분, 60분 중 하나여야 합니다.")
    public boolean isMaxWalkingMinutesAllowed() {
        return maxWalkingMinutes == null || Set.of(10, 30, 60).contains(maxWalkingMinutes);
    }
}
