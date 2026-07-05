package com.example.PartTrip.repository.community;

import com.example.PartTrip.entity.community.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findAllByOrderByCreateDateDesc();
}
