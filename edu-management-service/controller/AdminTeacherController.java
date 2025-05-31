package org.example.edumanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.enums.RoleType;
import org.example.edumanagementservice.service.UserService;
import org.example.edumanagementservice.util.AccountGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/admin/teacher")
@RequiredArgsConstructor
public class AdminTeacherController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addTeacher(@RequestBody Map<String, Object> params) {
        int year = (int) params.get("year");
        int deptId = (int) params.get("deptId");
        int index = (int) params.get("index");
        String account = AccountGenerator.generateTeacherAccount(year, deptId, index);
        userService.createUser(account, "123456", RoleType.TEACHER);
        return ResponseEntity.ok("教师账号创建成功: " + account);
    }

    @PostMapping("/batch")
    public ResponseEntity<?> batchAddTeachers(@RequestBody Map<String, Object> params) {
        int year = (int) params.get("year");
        int deptId = (int) params.get("deptId");
        int count = (int) params.get("count");

        List<String> createdAccounts = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String account = AccountGenerator.generateTeacherAccount(year, deptId, i);
            userService.createUser(account, "123456", RoleType.TEACHER);
            createdAccounts.add(account);
        }
        return ResponseEntity.ok(createdAccounts);
    }


    @GetMapping
    public ResponseEntity<?> listTeachers() {
        return ResponseEntity.ok(userService.findByRole(RoleType.TEACHER));
    }

    @DeleteMapping("/{account}")
    public ResponseEntity<?> deleteTeacher(@PathVariable String account) {
        userService.deleteUser(account);
        return ResponseEntity.ok("删除成功");
    }
}