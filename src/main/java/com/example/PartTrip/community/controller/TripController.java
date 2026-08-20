package com.example.PartTrip.community.controller;

import com.example.PartTrip.community.dto.TripRequestDto;
import com.example.PartTrip.community.dto.TripResponseDto;
import com.example.PartTrip.community.service.TripService;
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

    // 일정 수정 (본인 것만 가능)
    @PutMapping("/{tripId}")
    public TripResponseDto updateTrip(
            Authentication authentication,
            @PathVariable Long tripId,
            @RequestBody TripRequestDto dto
    ) {
        return tripService.updateTrip(authentication.getName(), tripId, dto);
    }

    // 일정 삭제 (본인 것만 가능)
    @DeleteMapping("/{tripId}")
    public String deleteTrip(Authentication authentication, @PathVariable Long tripId) {
        tripService.deleteTrip(authentication.getName(), tripId);
        return "일정이 삭제되었습니다.";
    }
}
