package com.example.PartTrip.service.main;

import com.example.PartTrip.dto.main.PopulationInfoResponseDto;
import com.example.PartTrip.entity.main.PopulationInfoEntity;
import com.example.PartTrip.repository.main.PopulationInfoRepository;
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
                        population.getNationCode(),
                        population.getNationName(),
                        population.getPercent()
                ))
                .toList();
    }
}