package org.example.edumanagementservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.PermissionDTO;
import org.example.edumanagementservice.service.PermissionService;
import org.example.edumanagementservice.util.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Tag(name = "权限管理", description = "权限的增删改查接口")
public class PermissionController {

    private final PermissionService permissionService;

    /* --------------------------- CRUD APIs --------------------------- */

    @PostMapping
    public BaseResponse<PermissionDTO> create(@RequestBody PermissionDTO dto) {
        try {
            PermissionDTO created = permissionService.createPermission(dto);
            return BaseResponse.success(created);
        } catch (Exception e) {
            return BaseResponse.fail(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public BaseResponse<PermissionDTO> update(@PathVariable Long id, @RequestBody PermissionDTO dto) {
        try {
            PermissionDTO updated = permissionService.updatePermission(id, dto);
            return updated != null
                    ? BaseResponse.success(updated, "修改成功")
                    : BaseResponse.fail(HttpStatus.NOT_FOUND, "未找到对应权限");
        } catch (Exception e) {
            return BaseResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> delete(@PathVariable Long id) {
        try {
            permissionService.deletePermission(id);
            return BaseResponse.success(null, "删除成功");
        } catch (Exception e) {
            return BaseResponse.fail(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public BaseResponse<PermissionDTO> getById(@PathVariable Long id) {
        try {
            PermissionDTO permission = permissionService.getPermissionById(id);
            return permission != null
                    ? BaseResponse.success(permission)
                    : BaseResponse.fail(HttpStatus.NOT_FOUND, "未找到对应权限");
        } catch (Exception e) {
            return BaseResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping
    public BaseResponse<List<PermissionDTO>> getAll() {
        try {
            List<PermissionDTO> list = permissionService.getAllPermissions();
            return BaseResponse.success(list);
        } catch (Exception e) {
            return BaseResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /* --------------------------- Admin APIs --------------------------- */

    @PostMapping("/assign")
    public BaseResponse<Boolean> assignPermission(@RequestParam Long teacherId, @RequestParam Boolean canPublish) {
        try {
            boolean result = permissionService.assignPermission(teacherId, canPublish);
            return BaseResponse.success(result, "权限分配成功");
        } catch (Exception e) {
            return BaseResponse.fail(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/course/approve")
    public BaseResponse<Boolean> approveCourse(@RequestParam Long courseId, @RequestParam Boolean approved) {
        try {
            boolean result = permissionService.approveCourse(courseId, approved);
            return BaseResponse.success(result, "课程审核操作成功");
        } catch (Exception e) {
            return BaseResponse.fail(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PostMapping("/course/publish")
    public BaseResponse<Boolean> publishCourse(@RequestParam Long courseId, @RequestParam Long teacherId) {
        try {
            boolean result = permissionService.publishCourse(courseId, teacherId);
            return BaseResponse.success(result, "课程发布成功");
        } catch (Exception e) {
            return BaseResponse.fail(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }
}
