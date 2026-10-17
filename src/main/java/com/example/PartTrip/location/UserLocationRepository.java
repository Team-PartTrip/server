package com.example.PartTrip.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDateTime;

public interface UserLocationRepository extends JpaRepository<UserLocationEntity, String> {

    @Modifying
    long deleteByRecordedAtBefore(LocalDateTime time);
}
