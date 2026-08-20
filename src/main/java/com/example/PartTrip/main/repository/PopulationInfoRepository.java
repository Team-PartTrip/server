package com.example.PartTrip.main.repository;

import com.example.PartTrip.main.entity.PopulationInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PopulationInfoRepository extends JpaRepository<PopulationInfoEntity, Long> {

    List<PopulationInfoEntity> findByCountryName(String countryName);
}