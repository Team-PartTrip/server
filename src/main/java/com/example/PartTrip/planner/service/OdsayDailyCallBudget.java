package com.example.PartTrip.planner.service;

import com.example.PartTrip.planner.repository.OdsayDailyCallUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

/** ODsay 호출 한도를 한국 날짜 기준으로 DB에 저장해 재시작·다중 인스턴스에도 적용한다. */
@Component
public class OdsayDailyCallBudget {

    private static final ZoneId KOREA = ZoneId.of("Asia/Seoul");

    private final OdsayDailyCallUsageRepository usageRepository;
    private final int dailyLimit;
    private final Clock clock;
    // 직접 생성하는 단위 테스트에서만 쓰는 로컬 카운터.
    private LocalDate countedDate;
    private int usedCalls;

    /** 실제 서버에서는 DB 기반 카운터를 사용한다. */
    @Autowired
    public OdsayDailyCallBudget(
            OdsayDailyCallUsageRepository usageRepository,
            @Value("${odsay.daily-call-limit:30}") int dailyLimit) {
        this.usageRepository = usageRepository;
        this.dailyLimit = Math.max(0, dailyLimit);
        this.clock = Clock.system(KOREA);
        this.countedDate = LocalDate.now(clock);
    }

    /** 고정 시계로 날짜 경계를 검증할 수 있도록 단위 테스트용 카운터를 만든다. */
    OdsayDailyCallBudget(int dailyLimit, Clock clock) {
        this.usageRepository = null;
        this.dailyLimit = Math.max(0, dailyLimit);
        this.clock = clock;
        this.countedDate = LocalDate.now(clock);
    }

    /** 로컬 카운터를 사용하는 단위 테스트용 생성자. */
    OdsayDailyCallBudget(int dailyLimit) {
        this(dailyLimit, Clock.system(KOREA));
    }

    /** 성공 여부와 무관하게 외부 요청 1건을 소진 처리한다. */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean tryAcquire() {
        if (dailyLimit == 0) return false;
        LocalDate today = LocalDate.now(clock);
        if (usageRepository != null) {
            return usageRepository.tryIncrement(today, dailyLimit) == 1;
        }
        synchronized (this) {
            if (!today.equals(countedDate)) {
                countedDate = today;
                usedCalls = 0;
            }
            if (usedCalls >= dailyLimit) return false;
            usedCalls++;
            return true;
        }
    }
}
