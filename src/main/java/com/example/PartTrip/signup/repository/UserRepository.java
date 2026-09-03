package com.example.PartTrip.signup.repository;

import com.example.PartTrip.signup.entity.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUserId(String userId);

    List<UserEntity> findAllByUserMailIgnoreCaseOrderByUserIdAsc(String userMail);

    // 아이디 중복 확인
    boolean existsByUserId(String userId);

    boolean existsByUserMailIgnoreCase(String userMail);

    boolean existsByNickName(String nickName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserEntity u WHERE u.userId IN :userIds ORDER BY u.userId")
    List<UserEntity> findAllByUserIdInForUpdate(
            @Param("userIds") Collection<String> userIds);
}
