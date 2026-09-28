package com.example.PartTrip.planner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/** 경로 API 호출을 일정 저장 요청과 분리된 제한된 스레드 풀에서 처리한다. */
@Configuration
@EnableAsync
public class PlannerRouteAsyncConfig {

    /** 비동기 경로 계산의 동시 실행 수와 대기열을 제한한다. */
    @Bean(name = "plannerRouteTaskExecutor")
    public Executor plannerRouteTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("planner-route-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(10);
        executor.initialize();
        return executor;
    }
}
