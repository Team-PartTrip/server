package com.example.PartTrip.repository.mission;

import com.example.PartTrip.entity.mission.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<MissionEntity, Long> {

    List<MissionEntity> findByUserId(String userId);

    List<MissionEntity> findByUserIdAndCompletedTrue(String userId);

    void deleteByUserId(String userId);

}