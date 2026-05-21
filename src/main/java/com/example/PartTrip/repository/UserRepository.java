package com.example.PartTrip.repository;

import com.example.PartTrip.entity.UserManage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserManage, Long > {
}
