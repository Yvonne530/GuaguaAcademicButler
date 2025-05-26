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
            // 1. 记录输入参数（脱敏处理）
            log.debug("开始管理员登录验证 - 用户名: {}, 密码长度: {}", username, password.length());

            // 2. 查询管理员账号
            Admin admin = adminRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.warn("管理员账号不存在 - username: {}", username);
                        return new CustomException("账号或密码错误");
                    });

            // 3. 验证账号状态（可选扩展）
            if (!isAccountActive(admin)) {
                log.warn("管理员账号已禁用 - username: {}", username);
                throw new CustomException("账号已被禁用");
            }

            // 4. 密码验证（核心逻辑）
            log.trace("开始密码验证 - 数据库哈希: {}", admin.getPassword());
            boolean isPasswordValid = passwordEncoder.matches(password, admin.getPassword());

            if (!isPasswordValid) {
                log.error("密码验证失败 - username: {}", username);
                throw new CustomException("账号或密码错误");
            }

            // 5. 生成JWT令牌
            String token = jwtTokenProvider.createToken(username, RoleType.ADMIN);
            log.info("管理员登录成功 - username: {}, token: {}", username, maskToken(token));

            return token;

        } catch (CustomException e) {
            // 已知业务异常直接抛出
            throw e;
        } catch (Exception e) {
            // 未知系统异常记录详细日志
            log.error("管理员登录系统异常 - username: {}, error: {}", username, e.getMessage(), e);
            throw new CustomException("系统繁忙，请稍后重试");
        }
    }

    // 辅助方法：账号状态检查（可根据实体类扩展）
    private boolean isAccountActive(Admin admin) {
        // 如果实体类有isActive字段，可在此验证
        return true; // 默认全部启用
    }

    // 辅助方法：令牌脱敏显示
    private String maskToken(String token) {
        if (token == null || token.length() < 10) return "invalid";
        return token.substring(0, 6) + "..." + token.substring(token.length() - 4);
    }
}