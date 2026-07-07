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
}