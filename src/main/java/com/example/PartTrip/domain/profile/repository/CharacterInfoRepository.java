package com.example.PartTrip.domain.profile.repository;

import com.example.PartTrip.domain.profile.entity.CharacterInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterInfoRepository extends JpaRepository<CharacterInfoEntity, Long> {

    Optional<CharacterInfoEntity> findByCharacterId(Long characterId);
}
