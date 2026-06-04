package com.example.PartTrip.repository.signup;

import com.example.PartTrip.entity.signup.PendingSignUpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingSignUpRepository extends JpaRepository<PendingSignUpEntity, String> {
}
