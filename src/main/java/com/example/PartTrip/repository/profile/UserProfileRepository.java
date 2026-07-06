package com.example.PartTrip.repository.profile;

import com.example.PartTrip.entity.signup.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUserId(String userId);

    boolean existsByNickNameAndUserIdNot(String nickName, String userId);
}
