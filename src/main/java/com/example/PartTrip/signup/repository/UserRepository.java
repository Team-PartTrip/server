package com.example.PartTrip.signup.repository;

import com.example.PartTrip.signup.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUserId(String userId);

    List<UserEntity> findAllByUserMailIgnoreCaseOrderByUserIdAsc(String userMail);

    // 아이디 중복 확인
    boolean existsByUserId(String userId);

    boolean existsByUserMailIgnoreCase(String userMail);

    boolean existsByNickName(String nickName);
}
