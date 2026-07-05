package com.example.PartTrip.service.main;

import com.example.PartTrip.dto.main.CountryInfoResponseDto;
import com.example.PartTrip.dto.main.FestivalResponseDto;
import com.example.PartTrip.dto.main.PopulationInfoResponseDto;
import com.example.PartTrip.entity.main.CountryInfoEntity;
import com.example.PartTrip.entity.main.FestivalEntity;
import com.example.PartTrip.entity.main.PopulationInfoEntity;
import com.example.PartTrip.repository.main.CountryInfoRepository;
import com.example.PartTrip.repository.main.FestivalRepository;
import com.example.PartTrip.repository.main.PopulationInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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


    // 인구 구성 조회
    public List<PopulationInfoResponseDto> getPopulationInfo(String countryName) {

        List<PopulationInfoEntity> populations =
                populationInfoRepository.findByCountryName(countryName);

        return populations.stream()
                .map(population -> new PopulationInfoResponseDto(
                        population.getNationCode(),
                        population.getNationName(),
                        population.getPercent()
                ))
                .toList();
    }

    // 축제 조회
    public List<FestivalResponseDto> getFestivals(String countryName) {

        List<FestivalEntity> festivals =
                festivalRepository.findByCountryName(countryName);

        return festivals.stream()
                .map(festival -> new FestivalResponseDto(
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
