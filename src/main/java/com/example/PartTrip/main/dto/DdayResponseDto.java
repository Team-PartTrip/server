package com.example.PartTrip.main.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import com.example.PartTrip.planner.dto.response.PlannerScheduleResponseDto;

@Getter
public class DdayResponseDto {

    private String regionName;

    private String cityName;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer headcount;

    private String dday;

    private TripPhase status;

    /** 여행 중인 날의 카드. 일정이 없거나 여행 중이 아니면 빈 목록이다. */
    private List<PlannerScheduleResponseDto.Slot> todaySchedule = List.of();

    public DdayResponseDto(String regionName, String cityName, LocalDate startDate,
            LocalDate endDate, Integer headcount, String dday, TripPhase status) {
        this.regionName = regionName;
        this.cityName = cityName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.headcount = headcount;
        this.dday = dday;
        this.status = status;
    }

    /** 조회된 오늘 카드의 순서를 그대로 유지한다. */
    public void setTodaySchedule(List<PlannerScheduleResponseDto.Slot> schedule) {
        this.todaySchedule = List.copyOf(schedule);
    }
}
