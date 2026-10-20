package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.MoreTourPlacesResponseDto;
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
    private final TourPlaceImportService tourPlaceImportService;

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

        // 여행지 검색이 나라 전체로 넓어져서, 아직 안 받아온 도시도 고를 수
        // 있게 됐다. 비어 있으면 그 자리에서 받아온다. 이미 있는 도시는
        // 건드리지 않으므로 두 번째부터는 그냥 조회다.
        if (places.isEmpty() && city != null
                && tourPlaceImportService.importCityIfEmpty(countryName, city)) {
            places = tourPlaceRepository.search(countryName, city, tourPlaceCategory);
        }

        return places.stream().map(TourPlaceService::toDto).toList();
    }

    public MoreTourPlacesResponseDto getMoreTourPlace(String countryName,
                                                      String cityName,
                                                      String category,
                                                      String cursor) {
        TourPlaceCategory tourPlaceCategory = TourPlaceCategory.from(category);
        if (tourPlaceCategory == null) {
            // 카테고리마다 검색어가 달라서, 전체를 한꺼번에 더 받을 수는 없다
            throw new IllegalArgumentException("카테고리를 입력해주세요.");
        }
        if (cityName == null || cityName.isBlank()) {
            throw new IllegalArgumentException("도시를 입력해주세요.");
        }

        TourPlaceImportService.MoreResult result = tourPlaceImportService.fetchMore(
                countryName, cityName.trim(), tourPlaceCategory, cursor);
        return new MoreTourPlacesResponseDto(
                result.places().stream().map(TourPlaceService::toDto).toList(),
                result.cursor());
    }

    private static TourPlaceResponseDto toDto(TourPlaceEntity place) {
        return new TourPlaceResponseDto(
                place.getTourPlaceId(),
                place.getPlaceName(),
                place.getCategory() == null ? null : place.getCategory().getLabel(),
                place.getDescription(),
                place.getAddress(),
                place.getRating(),
                place.getImageUrl(),
                place.getLatitude(),
                place.getLongitude());
    }

}
