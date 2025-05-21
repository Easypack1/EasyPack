package com.example.Backend.controller;

import com.example.Backend.config.JwtUtil;
import com.example.Backend.dto.CommentRequestDTO;
import com.example.Backend.dto.CommentResponseDTO;
import com.example.Backend.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil;

    public CommentController(CommentService commentService, JwtUtil jwtUtil) {
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * ✅ 댓글 작성
     */
    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<?> addComment(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId,
            @RequestBody CommentRequestDTO requestDTO
    ) {
        String userId = jwtUtil.validateTokenAndGetUserId(token.replace("Bearer ", ""));
        CommentResponseDTO responseDTO = commentService.addComment(postId, userId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * ✅ 댓글 목록 조회
     */
    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getComments(@PathVariable Long postId) {
        List<CommentResponseDTO> commentList = commentService.getComments(postId);
        return ResponseEntity.ok(commentList);
    }

    /**
     * ✅ 댓글 삭제
     */
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(
            @RequestHeader("Authorization") String token,
            @PathVariable Long commentId
    ) {
        String userId = jwtUtil.validateTokenAndGetUserId(token.replace("Bearer ", ""));
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
