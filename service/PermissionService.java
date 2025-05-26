package org.example.edumanagementservice.service;

import org.example.edumanagementservice.dto.PermissionDTO;

import java.util.List;

public interface PermissionService {

    PermissionDTO createPermission(PermissionDTO dto);

    PermissionDTO updatePermission(Long id, PermissionDTO dto);

    void deletePermission(Long id);

    PermissionDTO getPermissionById(Long id);

    List<PermissionDTO> getAllPermissions();

    boolean assignPermission(Long teacherId, Boolean canPublish);

    boolean approveCourse(Long courseId, boolean approved);

    boolean publishCourse(Long courseId, Long teacherId);
}
