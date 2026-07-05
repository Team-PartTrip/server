package com.example.PartTrip.service.main;

import com.example.PartTrip.dto.main.DdayResponseDto;
import com.example.PartTrip.dto.main.TravelPlanRequestDto;
import com.example.PartTrip.entity.main.TravelPlanEntity;
import com.example.PartTrip.repository.main.TravelPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;

    // 여행 일정 등록 또는 수정
    public DdayResponseDto saveTravelPlan(String userId, TravelPlanRequestDto dto) {

        TravelPlanEntity travelPlan = travelPlanRepository.findByUserId(userId)
                .orElse(new TravelPlanEntity());

        travelPlan.setUserId(userId);
        travelPlan.setCountryName(dto.getCountryName());
        travelPlan.setCityName(dto.getCityName());
        travelPlan.setStartDate(dto.getStartDate());
        travelPlan.setEndDate(dto.getEndDate());

        TravelPlanEntity savedTravelPlan = travelPlanRepository.save(travelPlan);

        return toDdayResponseDto(savedTravelPlan);
    }
}