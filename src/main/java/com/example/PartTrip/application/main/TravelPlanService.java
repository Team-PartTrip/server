package com.example.PartTrip.application.main;

import com.example.PartTrip.application.main.data.DdayResponseDto;
import com.example.PartTrip.application.main.data.TravelPlanRequestDto;
import com.example.PartTrip.domain.main.entity.CountryInfoEntity;
import com.example.PartTrip.domain.main.entity.TravelPlanEntity;
import com.example.PartTrip.domain.main.repository.CountryInfoRepository;
import com.example.PartTrip.domain.main.repository.TravelPlanRepository;
import com.example.PartTrip.application.mission.MissionService;
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

        String previousCountry = travelPlan.getCountryName();

        boolean countryChanged =
                !isNew &&
                        previousCountry != null &&
                        !previousCountry.equals(dto.getCountryName());

        travelPlan.setUserId(userId);
        travelPlan.setCountryName(dto.getCountryName());
        travelPlan.setCityName(dto.getCityName());
        travelPlan.setStartDate(dto.getStartDate());
        travelPlan.setEndDate(dto.getEndDate());

        TravelPlanEntity savedTravelPlan =
                travelPlanRepository.save(travelPlan);

        if (countryChanged) {
            // 국가가 바뀌면 기존 미션 초기화
            missionService.resetMission(
                    userId,
                    dto.getCountryName()
            );
        } else {
            // 신규 사용자 또는 기존 미션이 없는 사용자에게 생성
            missionService.createMissionIfMissing(
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