package com.example.PartTrip.application.main;

import com.example.PartTrip.application.main.data.TourPlaceResponseDto;
import com.example.PartTrip.domain.main.entity.TourPlaceEntity;
import com.example.PartTrip.domain.main.repository.TourPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourPlaceService {

    private final TourPlaceRepository tourPlaceRepository;

    // 관광지 조회
    public List<TourPlaceResponseDto> getTourPlace(String countryName) {

        // 해당 국가의 관광지 목록 조회
        List<TourPlaceEntity> places =
                tourPlaceRepository.findByCountryName(countryName);

        // Entity -> DTO 변환
        return places.stream()
                .map(place -> new TourPlaceResponseDto(
                        place.getPlaceName(),
                        place.getDescription(),
                        place.getImageUrl(),
                        place.getLatitude(),
                        place.getLongitude()
                ))
                .toList();
    }

}

