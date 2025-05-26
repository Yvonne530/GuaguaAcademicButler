package org.example.edumanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.CourseSelectionDTO;
import org.example.edumanagementservice.model.JwtUser;
import org.example.edumanagementservice.service.CourseSelectionService;
import org.example.edumanagementservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final CourseSelectionService courseSelectionService;
    private final UserService userService;

    @PostMapping("/select")
    public ResponseEntity<?> selectCourse(@RequestBody CourseSelectionDTO dto,
                                          @AuthenticationPrincipal JwtUser user) {
        courseSelectionService.selectCourse(user.getId(), dto);  // ✅ 使用 Long 类型 ID
        return ResponseEntity.ok("选课成功");}}

    // ✅ 用 ID 作为缓存 key 更稳定
