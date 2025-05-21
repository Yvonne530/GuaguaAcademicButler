package org.example.edumanagementservice.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.edumanagementservice.enums.RoleType;
import org.example.edumanagementservice.exception.CustomException;
import org.example.edumanagementservice.model.Admin;
import org.example.edumanagementservice.repository.AdminRepository;
import org.example.edumanagementservice.util.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("adminAuthStrategy")
@RequiredArgsConstructor
@Slf4j
public class AdminAuthStrategy implements AuthStrategy {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(String username, String password) {
        try {
            Admin admin = adminRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.warn("管理员登录失败: 用户名不存在 - {}", username);
                        return new CustomException("账号或密码错误");
                    });

            if (!passwordEncoder.matches(password, admin.getPassword())) {
                log.warn("管理员登录失败: 密码错误 - {}", username);
                throw new CustomException("账号或密码错误");
            }

            log.info("管理员登录成功: {}", username);
            return jwtTokenProvider.createToken(username, RoleType.ADMIN); // 使用枚举
        } catch (Exception e) {
            log.error("管理员登录异常: {}", e.getMessage(), e);
            throw e;
        }
    }
}