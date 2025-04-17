package com.example.Backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 내부 식별용 PK

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;  // 사용자 ID

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String travel_destination;

    @Column(nullable = false)
    private String airline;

    public User(String userId, String password, String nickname, String travelDestination, String airline) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.travel_destination = travelDestination;
        this.airline = airline;
    }
}
