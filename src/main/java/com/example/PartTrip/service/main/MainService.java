package com.example.PartTrip.service.main;

import com.example.PartTrip.dto.main.CountryInfoResponseDto;
import com.example.PartTrip.entity.main.CountryInfoEntity;
import com.example.PartTrip.repository.main.CountryInfoRepository;
import com.example.PartTrip.repository.main.FestivalRepository;
import com.example.PartTrip.repository.main.PopulationInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainService {

    private final CountryInfoRepository countryInfoRepository;
    private final PopulationInfoRepository populationInfoRepository;
    private final FestivalRepository festivalRepository;

    // 국가 정보 조회
    public CountryInfoResponseDto getCountryInfo(String countryName) {

        CountryInfoEntity country = countryInfoRepository.findByCountryName(countryName)
                .orElseThrow(() -> new IllegalArgumentException("국가 정보를 찾을 수 없습니다."));

        return new CountryInfoResponseDto(
                country.getCountryName(),
                country.getCityName(),
                country.getImageUrl(),
                country.getSummary()
        );
    }

}
