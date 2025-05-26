package org.example.edumanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.model.JwtUser;
import org.example.edumanagementservice.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/course-requests")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(
            @PathVariable Long id,
            @RequestParam Boolean approved,
            @AuthenticationPrincipal JwtUser user) {
        courseService.approveRequest(id, approved, user.getUsername());
        return ResponseEntity.ok("审批状态已更新");
    }
}
