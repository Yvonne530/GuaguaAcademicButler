package org.example.edumanagementservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.edumanagementservice.annotation.RateLimit;
import org.example.edumanagementservice.enums.RoleType;
import org.example.edumanagementservice.exception.CustomException;
import org.example.edumanagementservice.strategy.AuthStrategy;
import org.example.edumanagementservice.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RateLimit(key = "#request.username", value = 3) // 每个用户名每分钟限流3次
@PostMapping("/login")
@Operation(summary = "用户登录（管理员/教师/学生）")
public ResponseEntity<?> login(@RequestBody org.example.edumanagementservice.factory.AuthController.LoginRequest request) {
    AuthStrategy strategy = authStrategyFactory.getStrategy(request.getUserType());
    String accessToken = strategy.login(request.getUsername(), request.getPassword());
    String refreshToken = jwtTokenProvider.createRefreshToken(request.getUsername());

    TokenResponse tokenResponse = new TokenResponse();
    tokenResponse.setAccessToken(accessToken);
    tokenResponse.setRefreshToken(refreshToken);

    return ResponseUtil.success("登录成功", tokenResponse);
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

    return ResponseUtil.success("刷新成功", newAccessToken);
}
