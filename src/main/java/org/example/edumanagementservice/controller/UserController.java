package org.example.edumanagementservice.controller;

import org.example.edumanagementservice.annotation.CustomRoute;
import org.example.edumanagementservice.model.User;
import org.example.edumanagementservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    // ✅ 推荐使用构造器注入
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CustomRoute(value = "users", method = RequestMethod.GET)
    public List<User> getUsers(@RequestParam String dept) {
        // ✅ 正确地使用对象调用实例方法
        return userService.findByDept(dept);
    }
}
