package com.example.PartTrip.profile.repository;

import com.example.PartTrip.signup.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUserId(String userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from UserEntity u where u.userId = :userId")
    Optional<UserEntity> findByUserIdForUpdate(@Param("userId") String userId);

    boolean existsByNickNameAndUserIdNot(String nickName, String userId);
}
