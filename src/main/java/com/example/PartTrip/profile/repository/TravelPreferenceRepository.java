package com.example.PartTrip.profile.repository;

import com.example.PartTrip.profile.entity.TravelPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelPreferenceRepository
        extends JpaRepository<TravelPreferenceEntity, String> {
}
