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

    // 회원정보 수정 (닉네임, 항공사, 나라만)
    @PutMapping("/user/update")
    public ResponseEntity<?> updateUserInfo(
            @RequestHeader("Authorization") String token,
            @RequestBody UserUpdateRequest request
    ) {
        String userIdFromToken = jwtUtil.validateTokenAndGetUserId(token.replace("Bearer ", ""));

        if (userIdFromToken == null) {
            return ResponseEntity.status(403).body("권한 없음: 유효하지 않은 토큰");
        }

        // 수정 반영
        userService.updateUserInfo(userIdFromToken, request);

        // 수정된 사용자 정보 재조회
        User updatedUser = userService.findByUserId(userIdFromToken);
        UserResponseDTO dto = new UserResponseDTO(
                updatedUser.getUserId(),
                updatedUser.getNickname(),
                updatedUser.getTravelDestination(),
                updatedUser.getAirline()
        );

        return ResponseEntity.ok(dto); // 수정된 정보를 그대로 응답
    }
}
