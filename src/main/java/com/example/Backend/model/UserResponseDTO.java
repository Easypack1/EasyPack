package com.example.Backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {
    private String userId;
    private String nickname;
    private String travel_destination;
    private String airline;
}
