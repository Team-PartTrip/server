package com.example.PartTrip.main.controller;

import com.example.PartTrip.main.dto.PlaceSearchResponseDto;
import com.example.PartTrip.main.service.PlaceSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceSearchController {

    private final PlaceSearchService placeSearchService;

    @GetMapping("/search")
    public List<PlaceSearchResponseDto> search(@RequestParam(defaultValue = "") String q) {
        return placeSearchService.search(q);
    }
}
