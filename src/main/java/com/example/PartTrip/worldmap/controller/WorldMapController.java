package com.example.PartTrip.worldmap.controller;

import com.example.PartTrip.worldmap.dto.request.AcquireCountryRequestDto;
import com.example.PartTrip.worldmap.dto.response.AcquireCountryResponseDto;
import com.example.PartTrip.worldmap.dto.response.CountryTravelHistoryResponseDto;
import com.example.PartTrip.worldmap.dto.response.WorldMapResponseDto;
import com.example.PartTrip.worldmap.dto.response.WorldMapStatsResponseDto;
import com.example.PartTrip.worldmap.service.WorldMapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/world-map")
public class WorldMapController {

    private final WorldMapService worldMapService;

    @GetMapping
    public ResponseEntity<WorldMapResponseDto> getWorldMap(Authentication authentication) {
        return ResponseEntity.ok(worldMapService.getWorldMap(authentication.getName()));
    }

    @PostMapping("/countries")
    public ResponseEntity<AcquireCountryResponseDto> acquireCountry(
            Authentication authentication,
            @Valid @RequestBody AcquireCountryRequestDto request
    ) {
        return ResponseEntity.ok(
                worldMapService.acquireCountry(authentication.getName(), request.getTripId())
        );
    }

    @GetMapping("/countries/{countryCode}")
    public ResponseEntity<CountryTravelHistoryResponseDto> getCountryHistory(
            Authentication authentication,
            @PathVariable String countryCode
    ) {
        return ResponseEntity.ok(
                worldMapService.getCountryHistory(authentication.getName(), countryCode)
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<WorldMapStatsResponseDto> getStats(Authentication authentication) {
        return ResponseEntity.ok(worldMapService.getStats(authentication.getName()));
    }
}
