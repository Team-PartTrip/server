package com.example.PartTrip.location;

import com.example.PartTrip.location.LocationDtos.LocationResponse;
import com.example.PartTrip.location.LocationDtos.UpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** 여행 중 위치 공유 (#160) */
@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PutMapping("/api/location")
    public ResponseEntity<Void> update(Authentication authentication, @Valid @RequestBody UpdateRequest request) {
        locationService.update(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/api/location")
    public ResponseEntity<Void> stop(Authentication authentication) {
        locationService.stop(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    /** 보호자: 시니어의 마지막 위치. 없으면 204 */
    @GetMapping("/api/guardians/seniors/{seniorUserId}/location")
    public ResponseEntity<LocationResponse> seniorLocation(
            Authentication authentication, @PathVariable String seniorUserId) {
        return locationService.getForGuardian(seniorUserId, authentication.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
