package com.example.PartTrip.repository;

import com.example.PartTrip.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long > {

}
