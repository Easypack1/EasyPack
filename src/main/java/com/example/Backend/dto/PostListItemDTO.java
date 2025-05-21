package com.example.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PostListItemDTO {
    private Long id;
    private String title;
    private String contentSummary;
    private String authorNickname;
    private int rating;
    private int likeCount;
    private int commentCount;
    private String country;
    private LocalDateTime createdAt;
}
