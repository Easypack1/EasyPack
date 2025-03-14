package com.example.Backend.service;

import com.example.Backend.model.User;
import com.example.Backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ 허용된 여행지 및 항공사 목록 (프론트엔드와 동일하게 맞춰야 함)
    private static final List<String> VALID_TRAVEL_DESTINATIONS = List.of("베트남", "미국", "일본", "태국", "필리핀");
    private static final List<String> VALID_AIRLINES = List.of("대한항공", "아시아나항공", "제주항공", "티웨이항공", "진에어항공");

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String password, String nickname, String travelDestination, String airline) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // ✅ 여행지 및 항공사 값 검증
        if (!VALID_TRAVEL_DESTINATIONS.contains(travelDestination)) {
            throw new IllegalArgumentException("Invalid travel destination selected.");
        }
        if (!VALID_AIRLINES.contains(airline)) {
            throw new IllegalArgumentException("Invalid airline selected.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // 비밀번호 해싱
        user.setNickname(nickname);
        user.setTravelDestination(travelDestination);
        user.setAirline(airline);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
