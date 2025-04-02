package com.example.Backend.model;

public class UserUpdateRequest {
    private String userId;
    private String password;
    private String nickname;
    private String travelDestination;
    private String airline;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getTravelDestination() { return travelDestination; }
    public void setTravelDestination(String travelDestination) { this.travelDestination = travelDestination; }

    public String getAirline() { return airline; }
    public void setAirline(String airline) { this.airline = airline; }
}
