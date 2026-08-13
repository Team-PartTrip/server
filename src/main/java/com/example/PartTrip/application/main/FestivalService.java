package com.example.PartTrip.application.main;

import com.example.PartTrip.application.main.data.FestivalResponseDto;
import com.example.PartTrip.domain.main.entity.FestivalEntity;
import com.example.PartTrip.domain.main.repository.FestivalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private final FestivalRepository festivalRepository;

    // 축제 조회
    public List<FestivalResponseDto> getFestivals(String countryName) {

        List<FestivalEntity> festivals =
                festivalRepository.findByCountryName(countryName);

        return festivals.stream()
                .map(festival -> new FestivalResponseDto(
                        festival.getFestivalId(),
                        festival.getTitle(),
                        festival.getCategory(),
                        festival.getDescription(),
                        festival.getStartDate(),
                        festival.getStartTime(),
                        festival.getLocation(),
                        festival.getImageUrl()
                ))
                .toList();
    }
}