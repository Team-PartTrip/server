package com.example.PartTrip.signup.repository;

import com.example.PartTrip.signup.entity.PendingSignUpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingSignUpRepository extends JpaRepository<PendingSignUpEntity, String> {
}
