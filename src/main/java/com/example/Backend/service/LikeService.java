package com.example.Backend.service;

import com.example.Backend.model.Like;
import com.example.Backend.model.Post;
import com.example.Backend.repository.LikeRepository;
import com.example.Backend.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
    }

    /**
     * ✅ 좋아요 토글 (좋아요 누르면 추가, 다시 누르면 취소)
     * @return true → 좋아요 추가됨, false → 좋아요 취소됨
     */
    public boolean toggleLike(Long postId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        return likeRepository.findByPostAndUserId(post, userId).map(like -> {
            likeRepository.delete(like);
            return false;
        }).orElseGet(() -> {
            Like newLike = new Like();
            newLike.setPost(post);
            newLike.setUserId(userId);
            likeRepository.save(newLike);
            return true;
        });
    }

    /**
     * ✅ 현재 사용자가 해당 게시글에 좋아요 눌렀는지 확인
     */
    public boolean hasLiked(Long postId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        return likeRepository.findByPostAndUserId(post, userId).isPresent();
    }

    /**
     * ✅ 좋아요 개수 조회 (선택)
     */
    public int countLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        return likeRepository.countByPost(post);
    }
}
