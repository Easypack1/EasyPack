package com.example.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String content;
    private String authorNickname;
    private LocalDateTime createdAt;
}
