package org.example.edumanagementservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.enums.RoleType;
import org.example.edumanagementservice.model.User;
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
        if (userRepository.findByAccount(account).isPresent()) {
            throw new IllegalArgumentException("账号已存在");
        }

        User user = new User();
        user.setAccount(account);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);

        userRepository.save(user);
    }

    @Override
    public void updatePassword(String account, String newPassword) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public List<User> findByRole(RoleType role) {
        return userRepository.findByRole(role);
    }

    @Override
    public void deleteUser(String account) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        userRepository.delete(user);
    }

    @Override
    public List<User> findByDept(String dept) {
        return userRepository.findByDept(dept);
    }
}
