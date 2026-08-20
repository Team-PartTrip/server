package com.example.PartTrip.signup.repository;

import com.example.PartTrip.signup.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUserId(String userId);

    Optional<UserEntity> findByUserMail(String userMail);

    // 아이디 중복 확인
    boolean existsByUserId(String userId);
}