package org.example.edumanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.enums.RoleType;
import org.example.edumanagementservice.service.UserService;
import org.example.edumanagementservice.util.AccountGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/admin/teacher")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminTeacherController {

    private final UserService userService;

    /** 兼容数字/字符串两种 JSON 入参，避免 ClassCastException */
    private static int toInt(Object v) {
        if (v instanceof Number n) return n.intValue();
        return Integer.parseInt(String.valueOf(v).trim());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTeacher(@RequestBody Map<String, Object> params) {
        int year = toInt(params.get("year"));
        int deptId = toInt(params.get("deptId"));
        int index = toInt(params.get("index"));
        String account = AccountGenerator.generateTeacherAccount(year, deptId, index);
        try {
            userService.createUser(account, "123456", RoleType.TEACHER);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "code", 400,
                    "message", "教师账号 " + account + " 已存在，请更换序号后重试"));
        }
        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "教师账号创建成功",
                "data", account));
    }

    @PostMapping("/batch")
    public ResponseEntity<?> batchAddTeachers(@RequestBody Map<String, Object> params) {
        int year = toInt(params.get("year"));
        int deptId = toInt(params.get("deptId"));
        int count = toInt(params.get("count"));

        List<String> createdAccounts = new ArrayList<>();
        List<String> skipped = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String account = AccountGenerator.generateTeacherAccount(year, deptId, i);
            try {
                userService.createUser(account, "123456", RoleType.TEACHER);
                createdAccounts.add(account);
            } catch (IllegalArgumentException e) {
                skipped.add(account);
            }
        }
        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "成功创建 " + createdAccounts.size() + " 个，跳过已存在 " + skipped.size() + " 个",
                "data", Map.of("created", createdAccounts, "skipped", skipped)));
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