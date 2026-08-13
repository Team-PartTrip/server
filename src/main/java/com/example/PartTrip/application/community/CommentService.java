package com.example.PartTrip.application.community;

import com.example.PartTrip.application.community.data.CommentRequestDto;
import com.example.PartTrip.application.community.data.CommentResponseDto;
import com.example.PartTrip.domain.community.entity.CommentEntity;
import com.example.PartTrip.domain.signup.entity.UserEntity;
import com.example.PartTrip.domain.community.repository.BoardRepository;
import com.example.PartTrip.domain.community.repository.CommentRepository;
import com.example.PartTrip.domain.community.repository.ReviewRepository;
import com.example.PartTrip.domain.community.repository.TripRepository;
import com.example.PartTrip.domain.signup.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ReviewRepository reviewRepository;
    private final TripRepository tripRepository;

    // 댓글(또는 대댓글) 작성 (게시판/리뷰 공용)
    public CommentResponseDto createComment(
            String userId, String targetType, Long targetId, CommentRequestDto dto
    ) {
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (dto.getParentCommentId() != null) {
            CommentEntity parent = commentRepository.findById(dto.getParentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
            if (!parent.getTargetType().equals(targetType) || !parent.getTargetId().equals(targetId)) {
                throw new IllegalArgumentException("잘못된 답글 요청입니다.");
            }
        }

        CommentEntity comment = new CommentEntity();
        comment.setTargetType(targetType);
        comment.setTargetId(targetId);
        comment.setParentCommentId(dto.getParentCommentId());
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setCreateDate(LocalDateTime.now());

        CommentEntity saved = commentRepository.save(comment);

        return toDto(saved);
    }

    // 댓글 목록 (작성순, 대댓글은 parentCommentId로 클라이언트에서 그룹핑)
    public List<CommentResponseDto> getComments(String targetType, Long targetId) {
        return commentRepository.findByTargetTypeAndTargetIdOrderByCreateDateAsc(targetType, targetId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 댓글 수정 (본인 댓글만 가능)
    public CommentResponseDto updateComment(String userId, Long commentId, CommentRequestDto dto) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인이 작성한 댓글만 수정할 수 있습니다.");
        }

        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }

        comment.setContent(dto.getContent());

        CommentEntity saved = commentRepository.save(comment);

        return toDto(saved);
    }

    // 댓글 삭제 (댓글 작성자 본인 또는 게시글 작성자 본인이면 가능)
    public void deleteComment(String userId, Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        boolean isCommentOwner = comment.getUserId().equals(userId);
        boolean isPostOwner = isPostAuthor(userId, comment.getTargetType(), comment.getTargetId());

        if (!isCommentOwner && !isPostOwner) {
            throw new IllegalArgumentException("댓글 작성자 또는 게시글 작성자만 삭제할 수 있습니다.");
        }

        // 이 댓글에 달린 대댓글도 함께 삭제
        commentRepository.deleteByParentCommentId(commentId);
        commentRepository.delete(comment);
    }

    // 댓글이 달린 게시글(자유게시판/후기/일정)의 작성자인지 확인
    private boolean isPostAuthor(String userId, String targetType, Long targetId) {
        switch (targetType) {
            case "BOARD":
                return boardRepository.findById(targetId)
                        .map(board -> board.getUserId().equals(userId))
                        .orElse(false);
            case "REVIEW":
                return reviewRepository.findById(targetId)
                        .map(review -> review.getUserId().equals(userId))
                        .orElse(false);
            case "TRIP":
                return tripRepository.findById(targetId)
                        .map(trip -> trip.getUserId().equals(userId))
                        .orElse(false);
            default:
                return false;
        }
    }

    private CommentResponseDto toDto(CommentEntity comment) {
        String nickName = userRepository.findByUserId(comment.getUserId())
                .map(UserEntity::getNickName)
                .orElse("알 수 없음");

        return new CommentResponseDto(
                comment.getCommentId(),
                comment.getTargetType(),
                comment.getTargetId(),
                comment.getParentCommentId(),
                comment.getUserId(),
                nickName,
                comment.getContent(),
                comment.getCreateDate()
        );
    }
}
