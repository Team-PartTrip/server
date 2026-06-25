package com.example.PartTrip.repository.main;

import com.example.PartTrip.entity.main.PopulationInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PopulationInfoRepository extends JpaRepository<PopulationInfoEntity, Long> {

    List<PopulationInfoEntity> findByCountryName(String countryName);
}