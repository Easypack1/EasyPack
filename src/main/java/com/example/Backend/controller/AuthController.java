package com.example.Backend.controller;

import com.example.Backend.model.User;
import com.example.Backend.service.UserService;
import com.example.Backend.model.UserUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")  // 필요 시 변경 가능
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        String userId = request.get("user_id");
        String password = request.get("password");
        String nickname = request.get("nickname");
        String travelDestination = request.get("travel_destination");
        String airline = request.get("airline");

        try {
            User user = userService.register(userId, password, nickname, travelDestination, airline);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }
    @PutMapping("/user/update")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserUpdateRequest request) {
        userService.updateUserInfo(request);
        return ResponseEntity.ok("User info updated successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        String userId = request.get("user_id");
        String password = request.get("password");

        try {
            User user = userService.authenticate(userId, password);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인 실패: " + e.getMessage());
        }
    }
}
