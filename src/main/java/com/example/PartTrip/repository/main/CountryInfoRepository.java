package com.example.PartTrip.repository.main;

import com.example.PartTrip.entity.main.CountryInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryInfoRepository extends JpaRepository<CountryInfoEntity, Long> {

    Optional<CountryInfoEntity> findByCountryName(String countryName);
}