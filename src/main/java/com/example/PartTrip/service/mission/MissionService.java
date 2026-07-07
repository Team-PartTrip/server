package com.example.PartTrip.service.mission;

import com.example.PartTrip.dto.mission.MissionResponseDto;
import com.example.PartTrip.entity.mission.MissionEntity;
import com.example.PartTrip.repository.mission.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    // 미션 조회
    public List<MissionResponseDto> getMission(String userId) {

        return missionRepository.findByUserId(userId)
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

    // 미션 완료
    public void completeMission(Long missionId) {

        MissionEntity mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("미션이 존재하지 않습니다."));

        mission.setCompleted(true);
        mission.setCompletedDate(LocalDate.now());

        missionRepository.save(mission);
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

    // 기본 미션 생성
    public void createDefaultMission(String userId, String countryName){

        missionRepository.save(
                MissionEntity.builder()
                        .userId(userId)
                        .missionTitle("현지 음식 먹기")
                        .missionDescription("현지 음식을 먹어보세요.")
                        .missionCountry(countryName)
                        .completed(false)
                        .missionCategory("DEFAULT_MISSION")
                        .missionPoint(100)
                        .build()
        );

        missionRepository.save(
                MissionEntity.builder()
                        .userId(userId)
                        .missionTitle("유명 관광지 방문")
                        .missionDescription("대표 관광지를 방문해보세요.")
                        .missionCountry(countryName)
                        .completed(false)
                        .missionCategory("DEFAULT_MISSION")
                        .missionPoint(100)
                        .build()
        );

        missionRepository.save(
                MissionEntity.builder()
                        .userId(userId)
                        .missionTitle("사진 찍기")
                        .missionDescription("여행 사진을 찍어보세요.")
                        .missionCountry(countryName)
                        .completed(false)
                        .missionCategory("PHOTO_MISSION")
                        .missionPoint(200)
                        .build()
        );

    }

}