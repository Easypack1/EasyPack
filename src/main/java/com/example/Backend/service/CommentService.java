package com.example.Backend.service;

import com.example.Backend.dto.CommentRequestDTO;
import com.example.Backend.dto.CommentResponseDTO;
import com.example.Backend.model.Comment;
import com.example.Backend.model.Post;
import com.example.Backend.model.User;
import com.example.Backend.repository.CommentRepository;
import com.example.Backend.repository.PostRepository;
import com.example.Backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /**
     * ✅ 댓글 작성
     */
    public CommentResponseDTO addComment(Long postId, String userId, CommentRequestDTO dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setContent(dto.getContent());
        comment.setAuthorNickname(user.getNickname());

        Comment saved = commentRepository.save(comment);

        return new CommentResponseDTO(
                saved.getId(),
                saved.getContent(),
                saved.getAuthorNickname(),
                saved.getCreatedAt()
        );
    }

    /**
     * ✅ 댓글 목록 조회
     */
    public List<CommentResponseDTO> getComments(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);

        return comments.stream()
                .map(c -> new CommentResponseDTO(
                        c.getId(),
                        c.getContent(),
                        c.getAuthorNickname(),
                        c.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    /**
     * ✅ 댓글 삭제
     */
    public void deleteComment(Long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 없음"));
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (!comment.getAuthorNickname().equals(user.getNickname())) {
            throw new RuntimeException("삭제 권한 없음");
        }

        commentRepository.delete(comment);
    }
}
