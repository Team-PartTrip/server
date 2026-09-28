package com.example.PartTrip.planner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/** 일정 저장 응답을 기다리게 하지 않고 경로 계산 작업을 백그라운드에서 실행한다. */
@Component
@RequiredArgsConstructor
public class PlannerRouteTaskRunner {

    /** 경로 계산을 비동기 실행기에 전달한다. */
    @Async("plannerRouteTaskExecutor")
    public void submit(Runnable calculation) {
        calculation.run();
    }
}
