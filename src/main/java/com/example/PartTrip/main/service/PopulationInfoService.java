package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.PopulationInfoResponseDto;
import com.example.PartTrip.main.entity.PopulationInfoEntity;
import com.example.PartTrip.main.repository.PopulationInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopulationInfoService {

    private final PopulationInfoRepository populationInfoRepository;

    // 인구 구성 조회
    public List<PopulationInfoResponseDto> getPopulationInfo(String countryName) {

        List<PopulationInfoEntity> populations =
                populationInfoRepository.findByCountryName(countryName);

        return populations.stream()
                .map(population -> new PopulationInfoResponseDto(
                        population.getPopulationInfoId(),
                        population.getNationCode(),
                        population.getNationName(),
                        population.getPercent()
                ))
                .toList();
    }
}