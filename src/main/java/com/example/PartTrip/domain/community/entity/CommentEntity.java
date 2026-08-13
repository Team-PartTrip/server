package com.example.PartTrip.domain.community.entity;

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

    // 댓글이 달린 대상 타입: BOARD, REVIEW
    @Column(name = "target_type", nullable = false)
    private String targetType;

    // 댓글이 달린 대상 ID (boardId 또는 reviewId)
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    // 부모 댓글 ID (대댓글이면 값이 있고, 최상위 댓글이면 null)
    @Column(name = "parent_comment_id")
    private Long parentCommentId;

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
