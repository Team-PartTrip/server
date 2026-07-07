package com.example.PartTrip.service.main;

import com.example.PartTrip.dto.main.DdayResponseDto;
import com.example.PartTrip.dto.main.TravelPlanRequestDto;
import com.example.PartTrip.entity.main.CountryInfoEntity;
import com.example.PartTrip.entity.main.TravelPlanEntity;
import com.example.PartTrip.repository.main.CountryInfoRepository;
import com.example.PartTrip.repository.main.TravelPlanRepository;
import com.example.PartTrip.service.mission.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;
    private final CountryInfoRepository countryInfoRepository;
    private final MissionService missionService;

    // 여행 일정 등록 또는 수정
    public DdayResponseDto saveTravelPlan(String userId, TravelPlanRequestDto dto) {

        TravelPlanEntity travelPlan = travelPlanRepository.findByUserId(userId)
                .orElse(new TravelPlanEntity());

        // 처음 등록인지 확인
        boolean isNew = (travelPlan.getUserId() == null);

        // 국가 변경 여부 확인
        boolean countryChanged = !isNew &&
                !travelPlan.getCountryName().equals(dto.getCountryName());

        travelPlan.setUserId(userId);
        travelPlan.setCountryName(dto.getCountryName());
        travelPlan.setCityName(dto.getCityName());
        travelPlan.setStartDate(dto.getStartDate());
        travelPlan.setEndDate(dto.getEndDate());

        TravelPlanEntity savedTravelPlan = travelPlanRepository.save(travelPlan);

        // 처음 등록 또는 국가가 변경된 경우에만 미션 생성
        if (isNew || countryChanged) {
            missionService.resetMission(
                    userId,
                    dto.getCountryName()
            );
        }

        return toDdayResponseDto(savedTravelPlan);
    }

    // D-Day 조회
    public DdayResponseDto getDday(String userId) {

        TravelPlanEntity travelPlan = travelPlanRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 여행 일정이 없습니다."));

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
                dday
        );
    }

    public void changeTravelCountry(Long travelPlanId, Long countryInfoId){

        TravelPlanEntity travelPlan =
                travelPlanRepository.findById(travelPlanId)
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

        // 국가가 바뀐 경우에만 미션 초기화
        missionService.resetMission(
                travelPlan.getUserId(),
                country.getCountryName()
        );
    }
}