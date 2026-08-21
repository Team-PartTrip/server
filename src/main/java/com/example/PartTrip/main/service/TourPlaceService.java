package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.TourPlaceResponseDto;
import com.example.PartTrip.main.entity.TourPlaceEntity;
import com.example.PartTrip.main.enums.TourPlaceCategory;
import com.example.PartTrip.main.repository.TourPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourPlaceService {

    private final TourPlaceRepository tourPlaceRepository;

    // 관광지 조회 (도시 · 카테고리는 선택)
    public List<TourPlaceResponseDto> getTourPlace(String countryName,
                                                  String cityName,
                                                  String category) {

        // 지원하지 않는 카테고리면 여기서 400 으로 걸러진다
        TourPlaceCategory tourPlaceCategory = TourPlaceCategory.from(category);

        // 빈 문자열은 조건 없음으로 처리
        String city = (cityName == null || cityName.isBlank()) ? null : cityName.trim();

        List<TourPlaceEntity> places =
                tourPlaceRepository.search(countryName, city, tourPlaceCategory);

        // Entity -> DTO 변환
        return places.stream()
                .map(place -> new TourPlaceResponseDto(
                        place.getTourPlaceId(),
                        place.getPlaceName(),
                        place.getCategory() == null ? null : place.getCategory().getLabel(),
                        place.getDescription(),
                        place.getAddress(),
                        place.getRating(),
                        place.getImageUrl(),
                        place.getLatitude(),
                        place.getLongitude()
                ))
                .toList();
    }

}
