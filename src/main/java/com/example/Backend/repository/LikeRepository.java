package com.example.Backend.repository;

import com.example.Backend.model.Like;
import com.example.Backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUserId(Post post, String userId);
    int countByPost(Post post);
}
