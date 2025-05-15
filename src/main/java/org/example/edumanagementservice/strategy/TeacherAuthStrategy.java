package org.example.edumanagementservice.strategy;

import com.edu.management.model.Teacher;
import org.example.edumanagementservice.enums.RoleType; // 使用枚举替代字符串
import com.edu.management.repository.TeacherRepository;
import com.edu.management.exception.CustomException;
import org.example.edumanagementservice.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("teacherAuthStrategy")
@RequiredArgsConstructor
@Slf4j
public class TeacherAuthStrategy implements AuthStrategy {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(String username, String password) {
        try {
            Teacher teacher = teacherRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.warn("教师登录失败: 用户名不存在 - {}", username);
                        return new CustomException("账号或密码错误"); // 模糊提示增强安全性
                    });

            if (!passwordEncoder.matches(password, teacher.getPassword())) {
                log.warn("教师登录失败: 密码错误 - {}", username);
                throw new CustomException("账号或密码错误");
            }

            log.info("教师登录成功: {}", username);
            return jwtTokenProvider.createToken(username, RoleType.TEACHER); // 使用枚举
        } catch (Exception e) {
            log.error("教师登录异常: {}", e.getMessage(), e);
            throw e; // 重新抛出异常以统一处理
        }
    }
}