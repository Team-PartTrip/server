package com.example.PartTrip.signup.repository;

import com.example.PartTrip.signup.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, String> {
}