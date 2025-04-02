package com.example.Backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String userId;
    private String password;
    private String nickname;
    private String travelDestination;
    private String airline;
}
