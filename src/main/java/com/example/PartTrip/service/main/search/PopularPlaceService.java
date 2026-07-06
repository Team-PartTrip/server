package com.example.PartTrip.service.main.search;

import com.example.PartTrip.dto.main.search.PopularPlaceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularPlaceService {

    public List<PopularPlaceResponseDto> getPopularPlaces() {

        return List.of(

                new PopularPlaceResponseDto(
                        1L,
                        "일본",
                        "도쿄",
                        "https://flagcdn.com/w320/jp.png"
                ),

                new PopularPlaceResponseDto(
                        2L,
                        "싱가포르",
                        "Singapore",
                        "https://flagcdn.com/w320/sg.png"
                ),

                new PopularPlaceResponseDto(
                        3L,
                        "태국",
                        "Bangkok",
                        "https://flagcdn.com/w320/th.png"
                ),

                new PopularPlaceResponseDto(
                        4L,
                        "프랑스",
                        "Paris",
                        "https://flagcdn.com/w320/fr.png"
                )

        );

    }

}