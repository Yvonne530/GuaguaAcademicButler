package org.example.edumanagementservice.service;

import org.example.edumanagementservice.model.User;
import org.example.edumanagementservice.enums.RoleType;

import java.util.List;

public interface UserService {
    void createUser(String account, String rawPassword, RoleType role);
    List<User> findByRole(RoleType role);
    void deleteUser(String account);

    // 新增的方法，供 UserController 使用
    List<User> findByDept(String dept);
}
