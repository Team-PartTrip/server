package com.example.PartTrip.service.community;

import com.example.PartTrip.dto.community.CommentRequestDto;
import com.example.PartTrip.dto.community.CommentResponseDto;
import com.example.PartTrip.entity.community.BoardEntity;
import com.example.PartTrip.entity.community.CommentEntity;
import com.example.PartTrip.entity.signup.UserEntity;
import com.example.PartTrip.repository.community.BoardRepository;
import com.example.PartTrip.repository.community.CommentRepository;
import com.example.PartTrip.repository.signup.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    // 댓글 작성
    public CommentResponseDto createComment(String userId, Long boardId, CommentRequestDto dto) {
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }

        boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        CommentEntity comment = new CommentEntity();
        comment.setBoardId(boardId);
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setCreateDate(LocalDateTime.now());

        CommentEntity saved = commentRepository.save(comment);

        return toDto(saved);
    }

    // 게시글의 댓글 목록 (작성순)
    public List<CommentResponseDto> getComments(Long boardId) {
        return commentRepository.findByBoardIdOrderByCreateDateAsc(boardId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 댓글 삭제 (본인 댓글만 가능)
    public void deleteComment(String userId, Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    private CommentResponseDto toDto(CommentEntity comment) {
        String nickName = userRepository.findByUserId(comment.getUserId())
                .map(UserEntity::getNickName)
                .orElse("알 수 없음");

        return new CommentResponseDto(
                comment.getCommentId(),
                comment.getBoardId(),
                comment.getUserId(),
                nickName,
                comment.getContent(),
                comment.getCreateDate()
        );
    }
}
