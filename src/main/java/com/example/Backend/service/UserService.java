package com.example.Backend.service;

import com.example.Backend.model.User;
import com.example.Backend.model.UserUpdateRequest;
import com.example.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입
    public User register(String userId, String password, String nickname, String travelDestination, String airline) {
        if (userRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자 ID입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(userId, encodedPassword, nickname, travelDestination, airline);
        return userRepository.save(user);
    }

    // 로그인
    public User authenticate(String userId, String password) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 올바르지 않습니다.");
        }

        return user;
    }

    // 회원정보 조회
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // 🔧 회원정보 수정 기능
    public void updateUserInfo(String userId, UserUpdateRequest request) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getTravelDestination() != null) {
            user.setTravelDestination(request.getTravelDestination());
        }
        if (request.getAirline() != null) {
            user.setAirline(request.getAirline());
        }

        userRepository.save(user);
    }
}
