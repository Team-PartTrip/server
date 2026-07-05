package com.example.PartTrip.service.community;

import com.example.PartTrip.dto.community.BoardRequestDto;
import com.example.PartTrip.dto.community.BoardResponseDto;
import com.example.PartTrip.entity.community.BoardEntity;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    // 자유게시판 글 작성
    public BoardResponseDto createBoard(String userId, BoardRequestDto dto) {
        validate(dto);

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        BoardEntity board = new BoardEntity();
        board.setUserId(userId);
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setLikeCount(0);
        board.setCreateDate(LocalDateTime.now());

        BoardEntity saved = boardRepository.save(board);

        return toDto(saved);
    }

    // 전체 글 목록 (최신순)
    public List<BoardResponseDto> getBoards() {
        return boardRepository.findAllByOrderByCreateDateDesc()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 글 단건 조회
    public BoardResponseDto getBoard(Long boardId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return toDto(board);
    }

    // 글 수정 (본인 글만 가능)
    public BoardResponseDto updateBoard(String userId, Long boardId, BoardRequestDto dto) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!board.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인이 작성한 글만 수정할 수 있습니다.");
        }

        validate(dto);

        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setUpdateDate(LocalDateTime.now());

        BoardEntity saved = boardRepository.save(board);

        return toDto(saved);
    }

    // 글 삭제 (본인 글만 가능, 댓글도 함께 삭제)
    public void deleteBoard(String userId, Long boardId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!board.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인이 작성한 글만 삭제할 수 있습니다.");
        }

        commentRepository.deleteByBoardId(boardId);
        boardRepository.delete(board);
    }

    private void validate(BoardRequestDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
    }

    private BoardResponseDto toDto(BoardEntity board) {
        String nickName = userRepository.findByUserId(board.getUserId())
                .map(UserEntity::getNickName)
                .orElse("알 수 없음");

        long commentCount = commentRepository.countByBoardId(board.getBoardId());

        return new BoardResponseDto(
                board.getBoardId(),
                board.getUserId(),
                nickName,
                board.getTitle(),
                board.getContent(),
                board.getLikeCount(),
                commentCount,
                board.getCreateDate(),
                board.getUpdateDate()
        );
    }
}
