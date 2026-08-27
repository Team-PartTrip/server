package com.example.PartTrip.planner.controller;

import com.example.PartTrip.planner.service.VoteReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class VoteReminderController {

    private final VoteReminderService voteReminderService;

    @PostMapping("/{plannerId}/votes/remind")
    public ResponseEntity<Void> remind(
            Authentication authentication,
            @PathVariable Long plannerId
    ) {
        voteReminderService.remind(plannerId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
