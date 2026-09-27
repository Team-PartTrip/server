package com.example.PartTrip.planner.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

/** ODsay 호출 한도를 한 서버 인스턴스 안에서 보수적으로 지킨다. */
@Component
public class OdsayDailyCallBudget {

    private static final ZoneId KOREA = ZoneId.of("Asia/Seoul");

    private final int dailyLimit;
    private final Clock clock;
    private LocalDate countedDate;
    private int usedCalls;

    @Autowired
    public OdsayDailyCallBudget(
            @Value("${odsay.daily-call-limit:30}") int dailyLimit) {
        this(dailyLimit, Clock.system(KOREA));
    }

    OdsayDailyCallBudget(int dailyLimit, Clock clock) {
        this.dailyLimit = Math.max(0, dailyLimit);
        this.clock = clock;
        this.countedDate = LocalDate.now(clock);
    }

    /** 성공 여부와 무관하게 외부 요청 1건을 소진 처리한다. */
    public synchronized boolean tryAcquire() {
        LocalDate today = LocalDate.now(clock);
        if (!today.equals(countedDate)) {
            countedDate = today;
            usedCalls = 0;
        }
        if (usedCalls >= dailyLimit) return false;
        usedCalls++;
        return true;
    }
}
