package com.example.PartTrip.service.main.search;

import com.example.PartTrip.dto.main.search.PopularPlaceResponseDto;
import com.example.PartTrip.repository.main.CountryInfoRepository;
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