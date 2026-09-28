package com.example.PartTrip.planner.repository;

import com.example.PartTrip.planner.entity.OdsayDailyCallUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface OdsayDailyCallUsageRepository
        extends JpaRepository<OdsayDailyCallUsageEntity, LocalDate> {

    /** PostgreSQL 원자 upsert로 여러 서버 인스턴스의 호출 수를 함께 제한한다. */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = """
            INSERT INTO odsay_daily_call_usage (usage_date, call_count)
            VALUES (:usageDate, 1)
            ON CONFLICT (usage_date)
            DO UPDATE SET call_count = odsay_daily_call_usage.call_count + 1
            WHERE odsay_daily_call_usage.call_count < :dailyLimit
            """, nativeQuery = true)
    int tryIncrement(@Param("usageDate") LocalDate usageDate,
            @Param("dailyLimit") int dailyLimit);
}
