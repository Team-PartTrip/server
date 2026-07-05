package com.example.PartTrip.entity.community;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
public class CommentEntity {

    // 댓글 PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    // 댓글이 달린 게시글 ID
    @Column(name = "board_id", nullable = false)
    private Long boardId;

    // 작성자 ID
    @Column(name = "user_id", nullable = false)
    private String userId;

    // 댓글 내용
    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    // 작성일
    @Column(name = "create_date")
    private LocalDateTime createDate;
}
