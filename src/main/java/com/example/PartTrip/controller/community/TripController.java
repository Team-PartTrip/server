package com.example.PartTrip.controller.community;

import com.example.PartTrip.dto.community.TripRequestDto;
import com.example.PartTrip.dto.community.TripResponseDto;
import com.example.PartTrip.service.community.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    // 일정 생성
    @PostMapping
    public TripResponseDto createTrip(Authentication authentication, @RequestBody TripRequestDto dto) {
        return tripService.createTrip(authentication.getName(), dto);
    }

    // 내가 만든 일정 목록
    @GetMapping("/mine")
    public List<TripResponseDto> getMyTrips(Authentication authentication) {
        return tripService.getMyTrips(authentication.getName());
    }

    // 일정 상세 (본인 것이거나 공개된 것만 조회 가능)
    @GetMapping("/{tripId}")
    public TripResponseDto getTrip(Authentication authentication, @PathVariable Long tripId) {
        return tripService.getTrip(authentication.getName(), tripId);
    }
}
