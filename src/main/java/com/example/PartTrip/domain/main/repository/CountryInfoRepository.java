package com.example.PartTrip.domain.main.repository;

import com.example.PartTrip.domain.main.entity.CountryInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryInfoRepository extends JpaRepository<CountryInfoEntity, Long> {

    Optional<CountryInfoEntity> findByCountryName(String countryName);
}