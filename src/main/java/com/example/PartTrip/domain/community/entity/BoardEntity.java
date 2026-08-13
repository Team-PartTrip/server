package com.example.PartTrip.domain.community.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
public class BoardEntity {

    // 게시글 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    // 작성자 ID
    @Column(name = "user_id", nullable = false)
    private String userId;

    // 게시글 제목
    @Column(name = "title", nullable = false)
    private String title;

    // 게시글 내용
    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    // 좋아요 수
    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    // 작성일
    @Column(name = "create_date")
    private LocalDateTime createDate;

    // 수정일
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
