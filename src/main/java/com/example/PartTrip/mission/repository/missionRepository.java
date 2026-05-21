package com.example.PartTrip.mission.repository;

import com.example.PartTrip.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface missionRepository extends JpaRepository<Mission, Long> {
}
