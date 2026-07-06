package com.example.PartTrip.controller.main.search;

import com.example.PartTrip.dto.main.search.PopularPlaceResponseDto;
import com.example.PartTrip.service.main.search.PopularPlaceService;
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