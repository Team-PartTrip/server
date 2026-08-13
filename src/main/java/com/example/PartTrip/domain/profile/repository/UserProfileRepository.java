package com.example.PartTrip.domain.profile.repository;

import com.example.PartTrip.domain.signup.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUserId(String userId);

    boolean existsByNickNameAndUserIdNot(String nickName, String userId);
}
