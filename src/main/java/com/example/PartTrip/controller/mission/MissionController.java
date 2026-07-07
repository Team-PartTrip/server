package com.example.PartTrip.controller.mission;

import com.example.PartTrip.dto.mission.MissionResponseDto;
import com.example.PartTrip.service.mission.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mission")
public class MissionController {

    private final MissionService missionService;

    // 전체 미션 조회
    @GetMapping
    public List<MissionResponseDto> getMission(Authentication authentication) {

        String userId = authentication.getName();

        return missionService.getMission(userId);
    }

    // 완료한 미션 조회
    @GetMapping("/completed")
    public List<MissionResponseDto> getCompletedMission(Authentication authentication) {

        String userId = authentication.getName();

        return missionService.getCompletedMission(userId);
    }

    // 미션 완료
    @PatchMapping("/{missionId}")
    public void completeMission(
            @PathVariable Long missionId
    ) {

        missionService.completeMission(missionId);

    }

}