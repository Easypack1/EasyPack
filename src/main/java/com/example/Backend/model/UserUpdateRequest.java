package com.example.Backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    private String nickname;

    private String travel_destination;

    private String airline;
}
