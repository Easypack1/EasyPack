package com.example.Backend.controller;

import com.example.Backend.config.JwtUtil;
import com.example.Backend.dto.PostListItemDTO;
import com.example.Backend.dto.PostRequestDTO;
import com.example.Backend.dto.PostResponseDTO;
import com.example.Backend.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    public PostController(PostService postService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * ✅ 게시글 작성
     */
    @PostMapping("/post")
    public ResponseEntity<?> createPost(
            @RequestHeader("Authorization") String token,
            @RequestBody PostRequestDTO requestDTO
    ) {
        String userId = jwtUtil.validateTokenAndGetUserId(token.replace("Bearer ", ""));
        PostResponseDTO responseDTO = postService.createPost(userId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * ✅ 게시글 상세 조회
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        PostResponseDTO responseDTO = postService.getPostById(postId);
        return ResponseEntity.ok(responseDTO);
    }

    // 인기 게시글 조회
    @GetMapping("/posts/popular")
    public ResponseEntity<List<PostListItemDTO>> getPopularPosts() {
        List<PostListItemDTO> popularPosts = postService.getPopularPosts(5); // ← 상위 5개
        return ResponseEntity.ok(popularPosts);
    }


    /**
     * ✅ 게시글 수정
     */
    @PutMapping("/post/{postId}")
    public ResponseEntity<?> updatePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId,
            @RequestBody PostRequestDTO requestDTO
    ) {
        String userId = jwtUtil.validateTokenAndGetUserId(token.replace("Bearer ", ""));
        PostResponseDTO updated = postService.updatePost(postId, userId, requestDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * ✅ 게시글 삭제
     */
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> deletePost(
            @RequestHeader("Authorization") String token,
            @PathVariable Long postId
    ) {
        String userId = jwtUtil.validateTokenAndGetUserId(token.replace("Bearer ", ""));
        postService.deletePost(postId, userId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    @GetMapping("/posts/country/{country}") //나라별 게시글 목록 조회
    public ResponseEntity<List<PostListItemDTO>> getPostsByCountry(
            @PathVariable String country
    ) {
        List<PostListItemDTO> posts = postService.getPostsByCountry(country);
        return ResponseEntity.ok(posts);
    }

}
