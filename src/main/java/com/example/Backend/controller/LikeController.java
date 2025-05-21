package com.example.Backend.controller;

import com.example.Backend.config.JwtUtil;
import com.example.Backend.dto.LikeResponseDTO;
import com.example.Backend.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/post")
public class LikeController {

    private final LikeService likeService;
    private final JwtUtil jwtUtil;

    public LikeController(LikeService likeService, JwtUtil jwtUtil) {
        this.likeService = likeService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * ✅ 좋아요 토글 (누르면 좋아요 or 취소)
     */
    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeResponseDTO> toggleLike(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId
    ) {
        String userId = jwtUtil.validateTokenAndGetUserId(token.replace("Bearer ", ""));
        boolean liked = likeService.toggleLike(postId, userId);
        return ResponseEntity.ok(new LikeResponseDTO(liked));
    }

    /**
     * ✅ 좋아요 여부 조회
     */
    @GetMapping("/{postId}/like")
    public ResponseEntity<LikeResponseDTO> hasLiked(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId
    ) {
        String userId = jwtUtil.validateTokenAndGetUserId(token.replace("Bearer ", ""));
        boolean liked = likeService.hasLiked(postId, userId);
        return ResponseEntity.ok(new LikeResponseDTO(liked));
    }
}
