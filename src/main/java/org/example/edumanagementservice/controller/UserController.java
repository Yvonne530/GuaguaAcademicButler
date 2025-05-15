package org.example.edumanagementservice.controller;

import org.example.edumanagementservice.annotation.CustomRoute;
import org.example.edumanagementservice.model.User;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class UserController {
    @CustomRoute(value = "users", method = RequestMethod.GET)
    public List<User> getUsers(@RequestParam String dept) {
        return userService.findByDept(dept);
    }
}