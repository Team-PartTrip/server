package com.example.PartTrip.planner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/** 서버 재시작과 인스턴스 간에도 유지되는 날짜별 ODsay 호출 수. */
@Entity
@Table(name = "odsay_daily_call_usage")
@Getter
@NoArgsConstructor
public class OdsayDailyCallUsageEntity {

    @Id
    @Column(name = "usage_date")
    private LocalDate usageDate;

    @Column(name = "call_count", nullable = false)
    private int callCount;
}
