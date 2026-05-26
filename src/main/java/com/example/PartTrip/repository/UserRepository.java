package com.example.PartTrip.repository;

import com.example.PartTrip.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long > {

    Optional<UserEntity> findByUserId(Long userId);

    Optional<UserEntity> findByEmail(String userMail);

}
