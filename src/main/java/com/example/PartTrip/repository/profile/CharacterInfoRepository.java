package com.example.PartTrip.repository.profile;

import com.example.PartTrip.entity.profile.CharacterInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterInfoRepository extends JpaRepository<CharacterInfoEntity, Long> {

    Optional<CharacterInfoEntity> findByCharacterId(Long characterId);
}
