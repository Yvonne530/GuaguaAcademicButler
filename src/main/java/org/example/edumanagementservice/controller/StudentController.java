package org.example.edumanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.CourseSelectionDTO;
import org.example.edumanagementservice.model.JwtUser;
import org.example.edumanagementservice.service.CourseSelectionService;
import org.example.edumanagementservice.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final CourseSelectionService courseSelectionService;
    private final UserService userService;

    @PostMapping("/select")
    public ResponseEntity<?> selectCourse(@RequestBody CourseSelectionDTO dto,
                                          @AuthenticationPrincipal JwtUser user) {
        courseSelectionService.selectCourse(user.getUsername(), dto);
        return ResponseEntity.ok("选课成功");
    }

    @GetMapping("/schedule")
    @Cacheable(value = "studentSchedule", key = "#user.username")
    public ResponseEntity<?> getSchedule(@AuthenticationPrincipal JwtUser user) {
        return ResponseEntity.ok(courseSelectionService.getSchedule(user.getUsername()));
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> body,
                                            @AuthenticationPrincipal JwtUser user) {
        userService.updatePassword(user.getUsername(), body.get("password"));
        return ResponseEntity.ok("密码修改成功");
    }
}
