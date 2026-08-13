package com.example.PartTrip.domain.mission.repository;

import com.example.PartTrip.domain.mission.entity.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<MissionEntity, Long> {

    // 현재 여행 국가의 미션 조회
    List<MissionEntity> findByUserIdAndMissionCountry(
            String userId,
            String missionCountry
    );

    // 완료한 미션 조회
    List<MissionEntity> findByUserIdAndCompletedTrue(String userId);

    void deleteByUserId(String userId);

    boolean existsByUserId(String userId);
}