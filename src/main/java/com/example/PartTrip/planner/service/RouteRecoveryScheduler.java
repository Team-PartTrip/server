package com.example.PartTrip.planner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteRecoveryScheduler {

    private final PlannerScheduleRouteService routeService;

    @Scheduled(initialDelay = 60_000, fixedDelay = 60_000)
    public void recoverStuckRoutes() {
        routeService.recoverStuck();
    }
}
