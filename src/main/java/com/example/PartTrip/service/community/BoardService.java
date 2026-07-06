package com.example.PartTrip.service.community;

import com.example.PartTrip.dto.community.BoardRequestDto;
import com.example.PartTrip.dto.community.BoardResponseDto;
import com.example.PartTrip.dto.community.PageResponseDto;
import com.example.PartTrip.entity.community.BoardEntity;
import com.example.PartTrip.entity.community.PostImageEntity;
import com.example.PartTrip.entity.signup.UserEntity;
import com.example.PartTrip.repository.community.BoardRepository;
import com.example.PartTrip.repository.community.CommentRepository;
import com.example.PartTrip.repository.community.LikeRepository;
import com.example.PartTrip.repository.community.PostImageRepository;
import com.example.PartTrip.repository.signup.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private static final String TARGET_TYPE = "BOARD";

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;

    public BoardResponseDto createBoard(String userId, BoardRequestDto dto) {
        validate(dto);

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        BoardEntity board = new BoardEntity();
        board.setUserId(userId);
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setCreateDate(LocalDateTime.now());

        BoardEntity saved = boardRepository.save(board);

        saveImages(saved.getBoardId(), dto.getImages());

        return toDto(saved, userId);
    }

    public PageResponseDto<BoardResponseDto> getBoards(String currentUserId, int page, int size) {
        Page<BoardEntity> result = boardRepository.findAllByOrderByCreateDateDesc(PageRequest.of(page, size));
        return toPageDto(result, currentUserId);
    }

    public PageResponseDto<BoardResponseDto> getMyBoards(String userId, int page, int size) {
        Page<BoardEntity> result = boardRepository.findByUserIdOrderByCreateDateDesc(userId, PageRequest.of(page, size));
        return toPageDto(result, userId);
    }

    public BoardResponseDto getBoard(Long boardId, String currentUserId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return toDto(board, currentUserId);
    }

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

        postImageRepository.deleteByTargetTypeAndTargetId(TARGET_TYPE, boardId);
        saveImages(boardId, dto.getImages());

        return toDto(saved, userId);
    }

    public void deleteBoard(String userId, Long boardId) {
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!board.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인이 작성한 글만 삭제할 수 있습니다.");
        }

        commentRepository.deleteByTargetTypeAndTargetId(TARGET_TYPE, boardId);
        likeRepository.deleteByTargetTypeAndTargetId(TARGET_TYPE, boardId);
        postImageRepository.deleteByTargetTypeAndTargetId(TARGET_TYPE, boardId);
        boardRepository.delete(board);
    }

    private void saveImages(Long boardId, List<String> images) {
        if (images == null) return;
        int order = 0;
        for (String url : images) {
            if (url == null || url.isBlank()) continue;
            PostImageEntity image = new PostImageEntity();
            image.setTargetType(TARGET_TYPE);
            image.setTargetId(boardId);
            image.setImageUrl(url);
            image.setSortOrder(order++);
            postImageRepository.save(image);
        }
    }

    private void validate(BoardRequestDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
    }

    private PageResponseDto<BoardResponseDto> toPageDto(Page<BoardEntity> page, String currentUserId) {
        List<BoardResponseDto> content = page.getContent().stream()
                .map(b -> toDto(b, currentUserId))
                .collect(Collectors.toList());

        return new PageResponseDto<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext()
        );
    }

    private BoardResponseDto toDto(BoardEntity board, String currentUserId) {
        String nickName = userRepository.findByUserId(board.getUserId())
                .map(UserEntity::getNickName)
                .orElse("알 수 없음");

        long commentCount = commentRepository.countByTargetTypeAndTargetId(TARGET_TYPE, board.getBoardId());
        long likeCount = likeRepository.countByTargetTypeAndTargetId(TARGET_TYPE, board.getBoardId());
        boolean liked = currentUserId != null && likeRepository
                .findByTargetTypeAndTargetIdAndUserId(TARGET_TYPE, board.getBoardId(), currentUserId)
                .isPresent();

        List<String> images = postImageRepository
                .findByTargetTypeAndTargetIdOrderBySortOrderAsc(TARGET_TYPE, board.getBoardId())
                .stream()
                .map(PostImageEntity::getImageUrl)
                .collect(Collectors.toList());

        return new BoardResponseDto(
                board.getBoardId(),
                board.getUserId(),
                nickName,
                board.getTitle(),
                board.getContent(),
                images,
                likeCount,
                liked,
                commentCount,
                board.getCreateDate(),
                board.getUpdateDate()
        );
    }
}
