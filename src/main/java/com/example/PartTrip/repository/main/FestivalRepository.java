package com.example.PartTrip.repository.main;

import com.example.PartTrip.entity.main.FestivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FestivalRepository extends JpaRepository<FestivalEntity, Long> {

    List<FestivalEntity> findByCountryName(String countryName);
}