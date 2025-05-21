package com.example.Backend.repository;

import com.example.Backend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * ✅ 게시글별 댓글 목록 시간순 조회
     */
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    /**
     * ✅ 게시글별 댓글 개수 조회
     */
    int countByPost(com.example.Backend.model.Post post);
}
