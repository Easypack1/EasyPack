package com.example.Backend.service;

import com.example.Backend.model.User;
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

    public User register(String userId, String password, String nickname, String travelDestination, String airline) {
        if (userRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자 ID입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(userId, encodedPassword, nickname, travelDestination, airline);
        return userRepository.save(user);
    }

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
}
