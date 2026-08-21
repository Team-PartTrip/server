package com.example.PartTrip.profile.repository;

import com.example.PartTrip.profile.entity.TravelThemeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelThemeRepository extends JpaRepository<TravelThemeEntity, Long> {

    List<TravelThemeEntity> findAllByOrderByThemeIdAsc();
}
