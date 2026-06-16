package com.example.PartTrip.repository.profile;

import com.example.PartTrip.entity.profile.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, String> {

    boolean existsByNickNameAndUserIdNot(String nickName, String userId);
}
