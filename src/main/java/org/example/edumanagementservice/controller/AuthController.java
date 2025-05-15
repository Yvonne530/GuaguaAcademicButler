package org.example.edumanagementservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.annotation.RateLimit;
import org.example.edumanagementservice.enums.RoleType;
import org.example.edumanagementservice.exception.CustomException;
import org.example.edumanagementservice.factory.AuthStrategyFactory;
import org.example.edumanagementservice.strategy.AuthStrategy;
import org.example.edumanagementservice.util.JwtTokenProvider;
import org.example.edumanagementservice.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证模块")
public class AuthController {

    private final AuthStrategyFactory authStrategyFactory;
    private final JwtTokenProvider jwtTokenProvider;


    @RateLimit(key = "#request.username", value = 3) // 每个用户名每分钟限流3次
    @PostMapping("/login")
    @Operation(summary = "用户登录（管理员/教师/学生）")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AuthStrategy strategy = authStrategyFactory.getStrategy(request.getUserType());
        String accessToken = strategy.login(request.getUsername(), request.getPassword());
        String refreshToken = jwtTokenProvider.createRefreshToken(request.getUsername());
        return ResponseEntity.ok(ResponseUtil.success("登录成功", new TokenResponse()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestHeader("Authorization") String refreshToken,
            @RequestBody RefreshTokenRequest request
    ) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException("无效的Refresh Token");
        }
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        RoleType role = RoleType.fromUserType(request.getUserType()); // 转换角色类型
        String newAccessToken = jwtTokenProvider.createToken(username, role);
        return ResponseEntity.ok(ResponseUtil.success(newAccessToken));
    }

    @Data
    public static class LoginRequest {
        @NotBlank(message = "用户名不能为空")
        private String username;
        @NotBlank(message = "密码不能为空")
        private String password;
        @NotBlank(message = "用户类型不能为空（admin/teacher/student）")
        private String userType; // 建议后续改为枚举
    }

    @Data
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
        // 构造方法省略
    }
    @Data
    public static class RefreshTokenRequest {
        @NotBlank(message = "用户类型不能为空")
        private String userType;
    }

}