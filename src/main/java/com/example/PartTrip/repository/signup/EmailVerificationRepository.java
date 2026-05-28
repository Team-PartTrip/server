package com.example.PartTrip.repository.signup;

import com.example.PartTrip.entity.signup.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, String> {
}