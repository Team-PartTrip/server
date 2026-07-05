package com.example.PartTrip.controller.community;

import com.example.PartTrip.dto.community.ShareTripRequestDto;
import com.example.PartTrip.dto.community.TripResponseDto;
import com.example.PartTrip.service.community.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community/shared-trips")
public class SharedTripController {

    private final TripService tripService;

    // 내 일정을 커뮤니티에 공개 공유
    @PostMapping
    public TripResponseDto shareTrip(Authentication authentication, @RequestBody ShareTripRequestDto dto) {
        return tripService.shareTrip(authentication.getName(), dto.getTripId());
    }

    // 공유된 일정 목록 (커뮤니티 피드)
    @GetMapping
    public List<TripResponseDto> listSharedTrips() {
        return tripService.listSharedTrips();
    }

    // 공유된 일정 상세
    @GetMapping("/{tripId}")
    public TripResponseDto getSharedTripDetail(@PathVariable Long tripId) {
        return tripService.getSharedTripDetail(tripId);
    }

    // 다른 사람 일정 가져오기(복사)
    @PostMapping("/{tripId}/import")
    public TripResponseDto importTrip(Authentication authentication, @PathVariable Long tripId) {
        return tripService.importTrip(authentication.getName(), tripId);
    }
}
