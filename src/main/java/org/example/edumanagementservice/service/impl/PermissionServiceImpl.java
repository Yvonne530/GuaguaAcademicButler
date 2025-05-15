package org.example.edumanagementservice.service.impl;

import org.example.edumanagementservice.dto.PermissionDTO;
import com.edu.management.exception.CustomException;
import org.example.edumanagementservice.model.Permission;
import org.example.edumanagementservice.repository.PermissionRepository;
import org.example.edumanagementservice.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        if (permissionRepository.findByName(permissionDTO.getName()).isPresent()) {
            throw new CustomException("权限已存在: " + permissionDTO.getName());
        }
        Permission permission = Permission.builder()
                .name(permissionDTO.getName())
                .description(permissionDTO.getDescription())
                .build();
        permission = permissionRepository.save(permission);
        permissionDTO.setId(permission.getId());
        return permissionDTO;
    }

    @Override
    public PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new CustomException("未找到权限ID: " + id));
        BeanUtils.copyProperties(permissionDTO, permission, "id");
        return convertToDTO(permissionRepository.save(permission));
    }

    @Override
    public void deletePermission(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new CustomException("权限ID不存在: " + id);
        }
        permissionRepository.deleteById(id);
    }

    @Override
    public PermissionDTO getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new CustomException("未找到权限ID: " + id));
        return convertToDTO(permission);
    }

    @Override
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PermissionDTO convertToDTO(Permission permission) {
        return PermissionDTO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .build();
    }
}
