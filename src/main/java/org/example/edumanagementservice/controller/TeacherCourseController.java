package org.example.edumanagementservice.controller;

@RestController
@RequestMapping("/api/teacher/course")
@RequiredArgsConstructor
public class TeacherCourseController {

    private final PermissionService permissionService;

    // 教师发布课程
    @PostMapping("/publish")
    public ResponseEntity<?> publish(@RequestParam Integer courseId, @RequestParam Integer teacherId) {
        return ResponseEntity.ok(permissionService.publishCourse(courseId, teacherId));
    }
}
