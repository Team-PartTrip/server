package com.example.PartTrip.application.main.search;

import com.example.PartTrip.application.main.search.data.PopularPlaceResponseDto;
import com.example.PartTrip.domain.main.repository.CountryInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularPlaceService {

    private final CountryInfoRepository countryInfoRepository;

    public List<PopularPlaceResponseDto> getPopularPlaces() {

        return countryInfoRepository.findAll()
                .stream()
                .limit(4)
                .map(country -> new PopularPlaceResponseDto(
                        country.getCountryInfoId(),
                        country.getCountryName(),
                        country.getCityName(),
                        country.getImageUrl()
                ))
                .toList();
    }
}