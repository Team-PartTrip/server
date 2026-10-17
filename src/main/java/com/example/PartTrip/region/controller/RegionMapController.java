package com.example.PartTrip.region.controller;

import com.example.PartTrip.region.dto.response.RegionMapResponseDto;
import com.example.PartTrip.region.service.RegionMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/region-map")
public class RegionMapController {

    private final RegionMapService regionMapService;

    @GetMapping
    public ResponseEntity<RegionMapResponseDto> getRegionMap(Authentication authentication) {
        return ResponseEntity.ok(regionMapService.getRegionMap(authentication.getName()));
    }
}
