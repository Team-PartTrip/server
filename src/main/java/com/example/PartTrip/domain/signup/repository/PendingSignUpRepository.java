package com.example.PartTrip.domain.signup.repository;

import com.example.PartTrip.domain.signup.entity.PendingSignUpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingSignUpRepository extends JpaRepository<PendingSignUpEntity, String> {
}
