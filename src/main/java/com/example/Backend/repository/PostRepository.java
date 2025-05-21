package com.example.Backend.repository;

import com.example.Backend.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * ✅ 좋아요 수 내림차순 + 작성일 내림차순으로 정렬된 상위 게시글 목록 조회
     */
    @Query("SELECT p FROM Post p ORDER BY p.likeCount DESC, p.createdAt DESC")
    List<Post> findTopPopularPosts(Pageable pageable);

    // ✨ 다른 쿼리들 예: 나라별 조회 등 추가 가능
    // List<Post> findByCountryOrderByCreatedAtDesc(String country);
    List<Post> findByCountryOrderByCreatedAtDesc(String country);
}
