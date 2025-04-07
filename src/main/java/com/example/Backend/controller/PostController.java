package com.example.Backend.controller;

import com.example.Backend.config.JwtUtil;
import com.example.Backend.model.Post;
import com.example.Backend.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/community")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    public PostController(PostService postService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/post")
    public ResponseEntity<?> createPost(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> request
    ) {
        String userId = jwtUtil.validateTokenAndGetUserId(token.replace("Bearer ", ""));
        String title = request.get("title");
        String content = request.get("content");

        Post saved = postService.createPost(userId, title, content);
        return ResponseEntity.ok(saved);
    }
}
