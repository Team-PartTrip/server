package com.example.PartTrip.main.search.controller;

import com.example.PartTrip.main.search.dto.PopularPlaceResponseDto;
import com.example.PartTrip.main.search.service.PopularPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main/search")
public class PopularPlaceController {

    private final PopularPlaceService popularPlaceService;

    @GetMapping("/popular")
    public List<PopularPlaceResponseDto> getPopularPlaces(){

        return popularPlaceService.getPopularPlaces();

    }

}