package com.example.PartTrip.planner.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

/** 전체 일정 교체 요청. 슬롯 배열 순서가 저장 순서이며 null 장소는 빈 카드다. */
public record SavePlannerScheduleRequestDto(
        @NotNull @Size(max = 366) List<@NotNull @Valid Day> days
) {
    public record Day(@NotNull LocalDate date,
                      @NotNull @Size(max = 50) List<@NotNull @Valid Slot> slots) {}
    public record Slot(@Positive Long slotId, @Positive Long tourPlaceId) {}
}
