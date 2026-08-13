package com.example.PartTrip.domain.signup.repository;

import com.example.PartTrip.domain.signup.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, String> {
}