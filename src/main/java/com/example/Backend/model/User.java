package com.example.Backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname; // ✅ 닉네임 필드 추가

    @Column
    private String travelDestination; // ✅ 여행지 필드 추가

    @Column
    private String airline; // ✅ 항공사 필드 추가
}
