package org.example.edumanagementservice.service;

import org.example.edumanagementservice.dto.PermissionDTO;

import java.util.List;

public interface PermissionService {

    PermissionDTO createPermission(PermissionDTO permissionDTO);

    PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO);

    void deletePermission(Long id);

    PermissionDTO getPermissionById(Long id);

    List<PermissionDTO> getAllPermissions();

    boolean assignPermission(Integer teacherId, Boolean canPublish);
    boolean publishCourse(Integer courseId, Integer teacherId);
    boolean approveCourse(Integer courseId, boolean approved);
}
