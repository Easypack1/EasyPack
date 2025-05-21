package com.example.Backend.service;

import com.example.Backend.dto.PostListItemDTO;
import com.example.Backend.dto.PostRequestDTO;
import com.example.Backend.dto.PostResponseDTO;
import com.example.Backend.model.Post;
import com.example.Backend.model.User;
import com.example.Backend.repository.CommentRepository;
import com.example.Backend.repository.LikeRepository;
import com.example.Backend.repository.PostRepository;
import com.example.Backend.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository,
                       LikeRepository likeRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * ✅ 게시글 작성
     */
    public PostResponseDTO createPost(String userId, PostRequestDTO dto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setRating(dto.getRating());
        post.setAuthorNickname(user.getNickname());
        post.setCountry(user.getTravel_destination()); // 나라 정보 반영

        Post saved = postRepository.save(post);

        return toResponseDTO(saved);
    }

    /**
     * ✅ 게시글 단건 조회
     */
    public PostResponseDTO getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return toResponseDTO(post);
    }

    /**
     * ✅ 게시글 수정
     */
    public PostResponseDTO updatePost(Long postId, String userId, PostRequestDTO dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!post.getAuthorNickname().equals(user.getNickname())) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setRating(dto.getRating());

        Post updated = postRepository.save(post);

        return toResponseDTO(updated);
    }

    /**
     * ✅ 게시글 삭제
     */
    public void deletePost(Long postId, String userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!post.getAuthorNickname().equals(user.getNickname())) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    /**
     * ✅ 인기 게시글 조회 (좋아요 수 + 최신순 정렬)
     */
    public List<PostListItemDTO> getPopularPosts(int limit) {
        List<Post> posts = postRepository.findTopPopularPosts(PageRequest.of(0, limit));

        return posts.stream().map(post -> new PostListItemDTO(
                post.getId(),
                post.getTitle(),
                summarize(post.getContent()),
                post.getAuthorNickname(),
                post.getRating(),
                likeRepository.countByPost(post),
                commentRepository.countByPost(post),
                post.getCountry(),
                post.getCreatedAt()
        )).collect(Collectors.toList());
    }

    /**
     * ✅ 게시글 본문 요약 (최대 80자)
     */
    private String summarize(String content) {
        return content.length() > 80 ? content.substring(0, 80) + "..." : content;
    }

    /**
     * ✅ Post → PostResponseDTO 변환
     */
    private PostResponseDTO toResponseDTO(Post post) {
        int likeCount = likeRepository.countByPost(post);
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthorNickname(),
                post.getRating(),
                likeCount,
                post.getCreatedAt()
        );
    }

    public List<PostListItemDTO> getPostsByCountry(String country) {
        List<Post> posts = postRepository.findByCountryOrderByCreatedAtDesc(country);

        return posts.stream()
                .map(post -> new PostListItemDTO(
                        post.getId(),
                        post.getTitle(),
                        summarize(post.getContent()),
                        post.getAuthorNickname(),
                        post.getRating(),
                        likeRepository.countByPost(post),
                        commentRepository.countByPost(post),
                        post.getCountry(),
                        post.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

}
