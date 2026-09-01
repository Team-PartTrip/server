package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.service.PlannerDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerDeleteController {

    private final PlannerDeleteService plannerDeleteService;

    // 플래너 삭제 (API-005-12). 그룹장만 가능하다.
    // 확정으로 만들어진 여행 카드는 지우지 않고 연결만 끊는다.
    @DeleteMapping("/{plannerId}")
    public ResponseEntity<Void> deletePlanner(
            Authentication authentication,
            @PathVariable Long plannerId
    ) {
        plannerDeleteService.deletePlanner(plannerId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
