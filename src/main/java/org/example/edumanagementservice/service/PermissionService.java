package org.example.edumanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.PermissionDTO;
import org.example.edumanagementservice.model.TeacherPermission;
import org.example.edumanagementservice.repository.TeacherPermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PermissionService {

    PermissionDTO createPermission(PermissionDTO permissionDTO);

    PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO);

    void deletePermission(Long id);

    PermissionDTO getPermissionById(Long id);

    List<PermissionDTO> getAllPermissions();
}
@Service
@RequiredArgsConstructor
public class PermissionService {

    private final TeacherPermissionRepository permissionRepository;
    private final CourseRepository courseRepository;

    // 设置教师权限
    public TeacherPermission assignPermission(Integer teacherId, Boolean canPublish) {
        TeacherPermission permission = permissionRepository.findByTeacherId(teacherId)
                .orElse(new TeacherPermission(null, teacherId, canPublish));
        permission.setCanPublishCourse(canPublish);
        return permissionRepository.save(permission);
    }

    // 查询权限
    public Boolean checkPermission(Integer teacherId) {
        return permissionRepository.findByTeacherId(teacherId)
                .map(TeacherPermission::getCanPublishCourse)
                .orElse(false);
    }

    // 审核课程（管理员）
    public Course approveCourse(Integer courseId, boolean approved) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));
        if ("APPROVED".equals(course.getStatus()) || "REJECTED".equals(course.getStatus())) {
            throw new RuntimeException("课程已审核");
        }
        course.setStatus(approved ? "APPROVED" : "REJECTED");
        return courseRepository.save(course);
    }

    // 发布课程（教师，需有权限且课程已审核通过）
    public Course publishCourse(Integer courseId, Integer teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));

        if (!course.getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("无权操作他人课程");
        }

        if (!"APPROVED".equals(course.getStatus())) {
            throw new RuntimeException("课程尚未审核通过");
        }

        if (!checkPermission(teacherId)) {
            throw new RuntimeException("教师无发布权限");
        }

        course.setStatus("PUBLISHED");
        return courseRepository.save(course);
    }
}
