package com.example.PartTrip.main.search.service;

import com.example.PartTrip.main.search.dto.PopularPlaceResponseDto;
import com.example.PartTrip.main.repository.CountryInfoRepository;
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