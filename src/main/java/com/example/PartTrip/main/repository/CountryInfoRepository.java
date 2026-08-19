package com.example.PartTrip.main.repository;

import com.example.PartTrip.main.entity.CountryInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryInfoRepository extends JpaRepository<CountryInfoEntity, Long> {

    Optional<CountryInfoEntity> findByCountryName(String countryName);
}