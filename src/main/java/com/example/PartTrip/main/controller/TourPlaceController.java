package com.example.PartTrip.main.controller;

import com.example.PartTrip.main.dto.MoreTourPlacesResponseDto;
import com.example.PartTrip.main.dto.TourPlaceResponseDto;
import com.example.PartTrip.main.service.TourPlacePhotoService;
import com.example.PartTrip.main.service.TourPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class TourPlaceController {

    private final TourPlaceService tourPlaceService;
    private final TourPlacePhotoService tourPlacePhotoService;

    // 관광지 조회
    // 예) /api/main/tour-place?countryName=일본&cityName=오사카&category=맛집
    @GetMapping("/tour-place")
    public List<TourPlaceResponseDto> getTourPlace(
            @RequestParam String countryName,
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false) String category
    ) {
        return tourPlaceService.getTourPlace(countryName, cityName, category);
    }

    @GetMapping("/tour-place/more")
    public MoreTourPlacesResponseDto getMoreTourPlace(
            @RequestParam String countryName,
            @RequestParam String cityName,
            @RequestParam String category,
            @RequestParam(required = false) String cursor
    ) {
        return tourPlaceService.getMoreTourPlace(countryName, cityName, category, cursor);
    }

    @GetMapping("/tour-place/{tourPlaceId}/photo")
    public ResponseEntity<Void> getPhoto(@PathVariable Long tourPlaceId) {
        return tourPlacePhotoService.currentUri(tourPlaceId)
                .map(uri -> ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(uri))
                        // 같은 사진을 앱이 다시 받지 않게 한다. 서버가 기억하는 시간보다 짧게
                        .cacheControl(CacheControl.maxAge(Duration.ofMinutes(50)).cachePrivate())
                        .<Void>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
