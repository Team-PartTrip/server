package com.example.PartTrip.controller.community;

import com.example.PartTrip.dto.community.BoardRequestDto;
import com.example.PartTrip.dto.community.BoardResponseDto;
import com.example.PartTrip.dto.community.CommentRequestDto;
import com.example.PartTrip.dto.community.CommentResponseDto;
import com.example.PartTrip.dto.community.PageResponseDto;
import com.example.PartTrip.service.community.BoardService;
import com.example.PartTrip.service.community.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class BoardController {

    private static final String TARGET_TYPE = "BOARD";

    private final BoardService boardService;
    private final CommentService commentService;

    // 자유게시판 글 작성
    @PostMapping("/boards")
    public BoardResponseDto createBoard(
            Authentication authentication,
            @RequestBody BoardRequestDto dto
    ) {
        return boardService.createBoard(authentication.getName(), dto);
    }

    // 글 목록 조회 (페이지네이션)
    @GetMapping("/boards")
    public PageResponseDto<BoardResponseDto> getBoards(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return boardService.getBoards(authentication.getName(), page, size);
    }

    // 내가 쓴 글 목록
    @GetMapping("/boards/mine")
    public PageResponseDto<BoardResponseDto> getMyBoards(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return boardService.getMyBoards(authentication.getName(), page, size);
    }

    // 글 단건 조회
    @GetMapping("/boards/{boardId}")
    public BoardResponseDto getBoard(Authentication authentication, @PathVariable Long boardId) {
        return boardService.getBoard(boardId, authentication.getName());
    }

    // 글 수정
    @PutMapping("/boards/{boardId}")
    public BoardResponseDto updateBoard(
            Authentication authentication,
            @PathVariable Long boardId,
            @RequestBody BoardRequestDto dto
    ) {
        return boardService.updateBoard(authentication.getName(), boardId, dto);
    }

    // 글 삭제
    @DeleteMapping("/boards/{boardId}")
    public String deleteBoard(
            Authentication authentication,
            @PathVariable Long boardId
    ) {
        boardService.deleteBoard(authentication.getName(), boardId);
        return "게시글이 삭제되었습니다.";
    }

    // 댓글(또는 대댓글) 작성
    @PostMapping("/boards/{boardId}/comments")
    public CommentResponseDto createComment(
            Authentication authentication,
            @PathVariable Long boardId,
            @RequestBody CommentRequestDto dto
    ) {
        return commentService.createComment(authentication.getName(), TARGET_TYPE, boardId, dto);
    }

    // 댓글 목록 조회
    @GetMapping("/boards/{boardId}/comments")
    public List<CommentResponseDto> getComments(@PathVariable Long boardId) {
        return commentService.getComments(TARGET_TYPE, boardId);
    }

    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public CommentResponseDto updateComment(
            Authentication authentication,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto dto
    ) {
        return commentService.updateComment(authentication.getName(), commentId, dto);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(
            Authentication authentication,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(authentication.getName(), commentId);
        return "댓글이 삭제되었습니다.";
    }
}
