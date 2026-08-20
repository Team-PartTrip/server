package com.example.PartTrip.main.repository;

import com.example.PartTrip.main.entity.FestivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FestivalRepository extends JpaRepository<FestivalEntity, Long> {

    List<FestivalEntity> findByCountryName(String countryName);
}