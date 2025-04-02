package com.example.Backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @JsonProperty("user_id")
    private String userId;

    private String password;

    private String nickname;

    @JsonProperty("travel_destination")
    private String travelDestination;

    private String airline;
}
