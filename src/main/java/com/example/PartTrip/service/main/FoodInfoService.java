package com.example.PartTrip.service.main;

import com.example.PartTrip.dto.main.FoodInfoResponseDto;
import com.example.PartTrip.entity.main.FoodInfoEntity;
import com.example.PartTrip.repository.main.FoodInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodInfoService {

    private final FoodInfoRepository foodInfoRepository;

    // 대표 음식 조회
    public List<FoodInfoResponseDto> getFoodInfo(String countryName) {

        List<FoodInfoEntity> foods =
                foodInfoRepository.findByCountryName(countryName);

        return foods.stream()
                .map(food -> new FoodInfoResponseDto(
                        food.getFoodName(),
                        food.getDescription(),
                        food.getImageUrl()
                ))
                .toList();
    }
}