package org.example.edumanagementservice.strategy;

import org.example.edumanagementservice.enums.RoleType;
import org.example.edumanagementservice.exception.CustomException;
import org.example.edumanagementservice.model.Student;
import org.example.edumanagementservice.repository.StudentRepository;
import org.example.edumanagementservice.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("studentAuthStrategy")
@RequiredArgsConstructor
@Slf4j
public class StudentAuthStrategy implements AuthStrategy {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(String username, String password) {
        try {
            Student student = studentRepository.findByAccount(username)
                    .orElseThrow(() -> {
                        log.warn("学生登录失败: 账号不存在 - {}", username);
                        return new CustomException("账号或密码错误");
                    });

            if (!passwordEncoder.matches(password, student.getPassword())) {
                log.warn("学生登录失败: 密码错误 - {}", username);
                throw new CustomException("账号或密码错误");
            }

            log.info("学生登录成功: {}", username);
            return jwtTokenProvider.createToken(username, RoleType.STUDENT);
        } catch (Exception e) {
            log.error("学生登录异常: {}", e.getMessage(), e);
            throw e;
        }
    }
}
