package com.example.Backend.controller;

import com.example.Backend.config.JwtUtil;
import com.example.Backend.model.User;
import com.example.Backend.model.UserResponseDTO;
import com.example.Backend.model.UserUpdateRequest;
import com.example.Backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        try {
            User user = userService.register(
                    request.get("user_id"),
                    request.get("password"),
                    request.get("nickname"),
                    request.get("travel_destination"),
                    request.get("airline")
            );
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        try {
            User user = userService.authenticate(request.get("user_id"), request.get("password"));
            String token = jwtUtil.generateToken(user.getUserId());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "user", user
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인 실패: " + e.getMessage());
        }
    }

    // 회원정보 조회
    @GetMapping("/user/me")
    public ResponseEntity<?> getMyInfo(@RequestHeader("Authorization") String token) {
        String userId = jwtUtil.validateTokenAndGetUserId(token.replace("Bearer ", ""));
        User user = userService.findByUserId(userId);

        UserResponseDTO dto = new UserResponseDTO(
                user.getUserId(),
                user.getNickname(),
                user.getTravelDestination(),
                user.getAirline()
        );

        return ResponseEntity.ok(dto);
    }


    @PutMapping("/user/update")
    public ResponseEntity<?> updateUserInfo(
            @RequestHeader("Authorization") String token,
            @RequestBody UserUpdateRequest request
    ) {
        String userIdFromToken = jwtUtil.validateTokenAndGetUserId(token.replace("Bearer ", ""));
        String userIdFromRequest = request.getUserId();

        // ✅ 디버깅 로그 출력
        System.out.println("🪪 userId from token: " + userIdFromToken);
        System.out.println("📦 userId from request: " + userIdFromRequest);

        if (userIdFromToken == null || !userIdFromToken.equals(userIdFromRequest)) {
            return ResponseEntity.status(403).body("권한 없음: userId 불일치");
        }

        userService.updateUserInfo(request);
        return ResponseEntity.ok("User info updated successfully");
    }
}
