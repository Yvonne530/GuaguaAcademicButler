package org.example.edumanagementservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.model.User;
import org.example.edumanagementservice.enums.RoleType;
import org.example.edumanagementservice.repository.UserRepository;
import org.example.edumanagementservice.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(String account, String rawPassword, RoleType role) {
        User user = User.builder()
                .account(account)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .build();
        userRepository.save(user);
    }

    @Override
    public List<User> findByRole(RoleType role) {
        return userRepository.findByRole(role);
    }

    @Override
    public void deleteUser(String account) {
        userRepository.deleteByAccount(account);
    }

    // ✅ 新增：支持通过部门查询用户（供 UserController 使用）
    @Override
    public List<User> findByDept(String dept) {
        return userRepository.findByDept(dept);
    }
}
