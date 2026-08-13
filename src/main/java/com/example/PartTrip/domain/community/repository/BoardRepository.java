package com.example.PartTrip.domain.community.repository;

import com.example.PartTrip.domain.community.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findAllByOrderByCreateDateDesc(Pageable pageable);
    Page<BoardEntity> findByUserIdOrderByCreateDateDesc(String userId, Pageable pageable);
}
