package com.example.PartTrip.main.service;

import com.example.PartTrip.main.dto.DdayResponseDto;
import com.example.PartTrip.main.dto.TravelPlanRequestDto;
import com.example.PartTrip.main.entity.CountryInfoEntity;
import com.example.PartTrip.main.entity.TravelPlanEntity;
import com.example.PartTrip.main.repository.CountryInfoRepository;
import com.example.PartTrip.main.repository.TravelPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;
    private final CountryInfoRepository countryInfoRepository;

    // 여행 일정 등록 또는 수정
    @Transactional
    public DdayResponseDto saveTravelPlan(
            String userId,
            TravelPlanRequestDto dto
    ) {

        TravelPlanEntity travelPlan = travelPlanRepository.findByUserId(userId)
                .orElse(null);

        boolean isNew = travelPlan == null;

        if (isNew) {
            travelPlan = new TravelPlanEntity();
        }

        travelPlan.setUserId(userId);
        travelPlan.setCountryName(dto.getCountryName());
        travelPlan.setCityName(dto.getCityName());
        travelPlan.setStartDate(dto.getStartDate());
        travelPlan.setEndDate(dto.getEndDate());
        travelPlan.setHeadcount(dto.getHeadcount());

        TravelPlanEntity savedTravelPlan =
                travelPlanRepository.save(travelPlan);

        return toDdayResponseDto(savedTravelPlan);
    }

    // D-Day 조회
    @Transactional(readOnly = true)
    public DdayResponseDto getDday(String userId) {

        TravelPlanEntity travelPlan = travelPlanRepository.findByUserId(userId)
                .orElse(null);

        // 등록된 여행 일정이 없으면 예외 대신 '쉬는 중' 상태를 내려줌
        // 앱 첫 진입 사용자가 대부분 이 경로를 탐
        if (travelPlan == null) {
            return new DdayResponseDto(null, null, null, null, null, "쉬는 중");
        }

        return toDdayResponseDto(travelPlan);
    }

    // Entity -> DdayResponseDto 변환
    private DdayResponseDto toDdayResponseDto(TravelPlanEntity travelPlan) {

        LocalDate today = LocalDate.now();

        long days = ChronoUnit.DAYS.between(today, travelPlan.getStartDate());

        String dday;

        if (days > 0) {
            dday = "D - " + days;
        } else if (days == 0) {
            dday = "D-Day";
        } else if (!today.isAfter(travelPlan.getEndDate())) {
            dday = "여행 중";
        } else {
            dday = "여행 종료";
        }

        return new DdayResponseDto(
                travelPlan.getCountryName(),
                travelPlan.getCityName(),
                travelPlan.getStartDate(),
                travelPlan.getEndDate(),
                travelPlan.getHeadcount(),
                dday
        );
    }

    @Transactional
    public void changeTravelCountry(String userId, Long travelPlanId, Long countryInfoId){

        // 본인 여행 계획이 아니면 조회되지 않으므로 남의 계획을 바꿀 수 없다
        TravelPlanEntity travelPlan =
                travelPlanRepository.findByTravelPlanIdAndUserId(travelPlanId, userId)
                        .orElseThrow(() -> new IllegalArgumentException("여행 계획이 없습니다."));

        CountryInfoEntity country =
                countryInfoRepository.findById(countryInfoId)
                        .orElseThrow(() -> new IllegalArgumentException("국가가 없습니다."));

        // 같은 나라면 아무것도 안 함
        if (travelPlan.getCountryName().equals(country.getCountryName())) {
            return;
        }

        travelPlan.setCountryName(country.getCountryName());
        travelPlan.setCityName(country.getCityName());

        travelPlanRepository.save(travelPlan);
    }
}