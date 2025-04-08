package com.example.Backend.service;

import com.example.Backend.model.Post;
import com.example.Backend.model.User;
import com.example.Backend.repository.PostRepository;
import com.example.Backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(String userId, String title, String content) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthorNickname(user.getNickname());

        return postRepository.save(post);
    }
}
