package org.example.edumanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teacher/course")
@RequiredArgsConstructor
public class TeacherCourseController {

    private final PermissionService permissionService;

    // 教师发布课程
    @PostMapping("/publish")
    public ResponseEntity<?> publish(@RequestParam Long courseId, @RequestParam Long teacherId) {
        return ResponseEntity.ok(permissionService.publishCourse(courseId, teacherId));
    }
}
