package org.example.edumanagementservice.factory;


import com.edu.management.service.factory.AuthStrategyFactory;
import org.example.edumanagementservice.strategy.AuthStrategy;
import com.edu.management.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证模块")
public class AuthController {

    private final AuthStrategyFactory authStrategyFactory;

    @PostMapping("/login")
    @Operation(summary = "用户登录（管理员/教师/学生）")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AuthStrategy strategy = authStrategyFactory.getStrategy(request.getUserType());
        String token = strategy.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(ResponseUtil.success("登录成功", token));
    }

    @Data
    public static class LoginRequest {
        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotBlank(message = "密码不能为空")
        private String password;

        @NotBlank(message = "用户类型不能为空（admin / teacher / student）")
        private String userType;
    }
}
