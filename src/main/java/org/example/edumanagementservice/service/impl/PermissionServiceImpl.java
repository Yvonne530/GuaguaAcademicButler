package org.example.edumanagementservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.PermissionDTO;
import org.example.edumanagementservice.model.Course;
import org.example.edumanagementservice.model.Permission;
import org.example.edumanagementservice.repository.CourseRepository;
import org.example.edumanagementservice.repository.PermissionRepository;
import org.example.edumanagementservice.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final CourseRepository courseRepository;

    @Override
    public PermissionDTO createPermission(PermissionDTO dto) {
        Permission entity = Permission.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        Permission saved = permissionRepository.save(entity);
        return new PermissionDTO(saved.getId(), saved.getName(), saved.getDescription());
    }

    @Override
    public PermissionDTO updatePermission(Long id, PermissionDTO dto) {
        Optional<Permission> optional = permissionRepository.findById(id);
        if (optional.isEmpty()) return null;

        Permission permission = optional.get();
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        Permission updated = permissionRepository.save(permission);
        return new PermissionDTO(updated.getId(), updated.getName(), updated.getDescription());
    }

    @Override
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public PermissionDTO getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .map(p -> new PermissionDTO(p.getId(), p.getName(), p.getDescription()))
                .orElse(null);
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(p -> new PermissionDTO(p.getId(), p.getName(), p.getDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean assignPermission(Integer teacherId, Boolean canPublish) {
        // 假设 assignPermission 功能不再适用于 Permission 实体，暂不实现
        throw new UnsupportedOperationException("权限分配功能不适用于 Permission 实体");
    }

    @Override
    public boolean approveCourse(Integer courseId, boolean approved) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));
        if ("APPROVED".equals(course.getStatus()) || "REJECTED".equals(course.getStatus())) {
            throw new RuntimeException("课程已审核");
        }
        course.setStatus(approved ? "APPROVED" : "REJECTED");
        courseRepository.save(course);
        return true;
    }
    @Override
    public boolean publishCourse(Integer courseId, Integer teacherId) {
        // 查找权限记录
        Permission permission = permissionRepository.findByName(teacherId.toString())
                .orElseThrow(() -> new RuntimeException("教师权限未配置"));

        if (!Boolean.parseBoolean(permission.getDescription())) {
            throw new RuntimeException("教师无权发布课程");
        }

        // 发布课程（此处你可以自行定义发布逻辑）
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));
        course.setStatus("PUBLISHED");
        courseRepository.save(course);

        return true;
    }
}
