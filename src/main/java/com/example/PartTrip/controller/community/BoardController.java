package com.example.PartTrip.controller.community;

import com.example.PartTrip.dto.community.BoardRequestDto;
import com.example.PartTrip.dto.community.BoardResponseDto;
import com.example.PartTrip.dto.community.CommentRequestDto;
import com.example.PartTrip.dto.community.CommentResponseDto;
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

    // 글 목록 조회
    @GetMapping("/boards")
    public List<BoardResponseDto> getBoards() {
        return boardService.getBoards();
    }

    // 글 단건 조회
    @GetMapping("/boards/{boardId}")
    public BoardResponseDto getBoard(@PathVariable Long boardId) {
        return boardService.getBoard(boardId);
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

    // 댓글 작성
    @PostMapping("/boards/{boardId}/comments")
    public CommentResponseDto createComment(
            Authentication authentication,
            @PathVariable Long boardId,
            @RequestBody CommentRequestDto dto
    ) {
        return commentService.createComment(authentication.getName(), boardId, dto);
    }

    // 댓글 목록 조회
    @GetMapping("/boards/{boardId}/comments")
    public List<CommentResponseDto> getComments(@PathVariable Long boardId) {
        return commentService.getComments(boardId);
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
