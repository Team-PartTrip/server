package com.example.PartTrip.presentation.main.search;

import com.example.PartTrip.application.main.search.data.PopularPlaceResponseDto;
import com.example.PartTrip.application.main.search.PopularPlaceService;
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