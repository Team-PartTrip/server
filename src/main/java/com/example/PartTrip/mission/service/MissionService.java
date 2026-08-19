package com.example.PartTrip.mission.service;

import com.example.PartTrip.mission.dto.MissionResponseDto;
import com.example.PartTrip.mission.entity.MissionEntity;
import com.example.PartTrip.main.repository.TravelPlanRepository;
import com.example.PartTrip.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final TravelPlanRepository travelPlanRepository;

    // 현재 여행 국가의 미션 조회
    public List<MissionResponseDto> getMission(String userId) {

        String missionCountry = travelPlanRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("여행 일정이 없습니다."))
                .getCountryName();

        return missionRepository.findByUserIdAndMissionCountry(
                        userId,
                        missionCountry
                )
                .stream()
                .map(this::toMissionResponseDto)
                .toList();
    }

    // 완료한 미션 조회
    public List<MissionResponseDto> getCompletedMission(String userId) {

        return missionRepository.findByUserIdAndCompletedTrue(userId)
                .stream()
                .map(this::toMissionResponseDto)
                .toList();
    }

    // 해당 사용자의 미션 존재 여부
    public boolean hasMission(String userId) {
        return missionRepository.existsByUserId(userId);
    }

    // 미션 완료
    public void completeMission(Long missionId) {

        MissionEntity mission = missionRepository.findById(missionId)
                .orElseThrow(() ->
                        new IllegalArgumentException("미션이 존재하지 않습니다.")
                );

        mission.setCompleted(true);
        mission.setCompletedDate(LocalDate.now());

        missionRepository.save(mission);
    }

    // 기본 미션 생성
    public void createDefaultMission(String userId, String countryName) {

        MissionEntity foodMission = MissionEntity.builder()
                .userId(userId)
                .missionTitle("현지 음식 먹기")
                .missionDescription("현지 음식을 먹어보세요.")
                .missionCountry(countryName)
                .completed(false)
                .isPassedCheck(false)
                .missionCategory("DEFAULT_MISSION")
                .missionPoint(100)
                .build();

        MissionEntity placeMission = MissionEntity.builder()
                .userId(userId)
                .missionTitle("유명 관광지 방문")
                .missionDescription("대표 관광지를 방문해보세요.")
                .missionCountry(countryName)
                .completed(false)
                .isPassedCheck(false)
                .missionCategory("DEFAULT_MISSION")
                .missionPoint(100)
                .build();

        MissionEntity photoMission = MissionEntity.builder()
                .userId(userId)
                .missionTitle("사진 찍기")
                .missionDescription("여행 사진을 찍어보세요.")
                .missionCountry(countryName)
                .completed(false)
                .isPassedCheck(false)
                .missionCategory("PHOTO_MISSION")
                .missionPoint(200)
                .build();

        missionRepository.saveAll(
                List.of(foodMission, placeMission, photoMission)
        );
    }

    // 국가 변경 시 기존 미션 삭제 후 새 미션 생성
    @Transactional
    public void resetMission(String userId, String countryName) {

        missionRepository.deleteByUserId(userId);
        createDefaultMission(userId, countryName);
    }

    // 미션이 없을 때만 생성
    public void createMissionIfMissing(String userId, String countryName) {

        if (!missionRepository.existsByUserId(userId)) {
            createDefaultMission(userId, countryName);
        }
    }

    // Entity -> DTO 변환
    private MissionResponseDto toMissionResponseDto(MissionEntity mission) {

        return new MissionResponseDto(
                mission.getMissionId(),
                mission.getMissionTitle(),
                mission.getMissionDescription(),
                mission.isCompleted(),
                mission.getMissionCountry(),
                mission.getMissionCategory(),
                mission.getMissionPoint(),
                mission.getImgUrl()
        );
    }
}