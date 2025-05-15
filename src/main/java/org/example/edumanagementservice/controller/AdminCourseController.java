package org.example.edumanagementservice.controller;
import org.example.edumanagementservice.service.CourseService;
import org.example.edumanagementservice.model.JwtUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
// AdminCourseController.java
@RestController
@RequestMapping("/admin/course-requests")
public class AdminCourseController {

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(
            @PathVariable Long id,
            @RequestParam Boolean approved,
            @AuthenticationPrincipal JwtUser user) {  // 修改参数类型
        CourseService.approveRequest(id, approved, user.getUsername());
        return ResponseEntity.ok("审批状态已更新");
    }
}
