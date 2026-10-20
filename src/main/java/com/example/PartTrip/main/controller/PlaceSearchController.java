package com.example.PartTrip.main.controller;

import com.example.PartTrip.main.dto.PlaceSearchResponseDto;
import com.example.PartTrip.main.service.AddressLookupService;
import com.example.PartTrip.main.service.PlaceSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceSearchController {

    private final PlaceSearchService placeSearchService;
    private final AddressLookupService addressLookupService;

    @GetMapping("/search")
    public List<PlaceSearchResponseDto> search(@RequestParam(defaultValue = "") String q) {
        return placeSearchService.search(q);
    }

    @GetMapping("/address")
    public Map<String, String> address(@RequestParam double latitude, @RequestParam double longitude) {
        Map<String, String> body = new HashMap<>();
        body.put("address", addressLookupService.lookup(latitude, longitude));
        return body;
    }
}
