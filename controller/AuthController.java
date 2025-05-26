package org.example.edumanagementservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.enums.RoleType;
import org.example.edumanagementservice.exception.CustomException;
import org.example.edumanagementservice.factory.AuthStrategyFactory;
import org.example.edumanagementservice.factory.TokenResponse;
import org.example.edumanagementservice.strategy.AuthStrategy;
import org.example.edumanagementservice.util.JwtTokenProvider;
import org.example.edumanagementservice.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor


public class AuthController {

    private final AuthStrategyFactory authStrategyFactory;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    @Operation(summary = "用户登录（管理员/教师/学生）")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AuthStrategy strategy = authStrategyFactory.getStrategy(request.getUserType());
        String accessToken = strategy.login(request.getUsername(), request.getPassword());
        String refreshToken = jwtTokenProvider.createRefreshToken(request.getUsername());

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken);

        return ResponseUtil.ok("登录成功", tokenResponse);
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新 Access Token")
    public ResponseEntity<?> refreshToken(
            @RequestHeader("Authorization") String refreshToken,
            @RequestBody RefreshTokenRequest request
    ) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException("无效的 Refresh Token");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        RoleType role = RoleType.fromUserType(request.getUserType());
        String newAccessToken = jwtTokenProvider.createToken(username, role);

        return ResponseUtil.ok("刷新成功", newAccessToken);
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
        private String userType; // admin / teacher / student
    }

    @Data
    public static class RefreshTokenRequest {
        private String userType;
    }
}
