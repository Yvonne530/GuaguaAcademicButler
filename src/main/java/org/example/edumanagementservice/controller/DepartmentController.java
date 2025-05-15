package org.example.edumanagementservice.controller;



import org.example.edumanagementservice.dto.DepartmentDTO;
import org.example.edumanagementservice.service.DepartmentService;
import org.example.edumanagementservice.util.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Department Controller", description = "院系管理接口")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public Object createDepartment(@RequestBody DepartmentDTO dto) {
        return ResponseUtil.success(String.valueOf(departmentService.createDepartment(dto)));
    }

    @PutMapping("/{id}")
    public Object updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO dto) {
        return ResponseUtil.success(String.valueOf(departmentService.updateDepartment(id, dto)));
    }

    @DeleteMapping("/{id}")
    public Object deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseUtil.success("删除成功");
    }

    @GetMapping("/{id}")
    public Object getDepartment(@PathVariable Long id) {
        return ResponseUtil.success(String.valueOf(departmentService.getDepartmentById(id)));
    }

    @GetMapping
    public Object listDepartments() {
        List<DepartmentDTO> list = departmentService.getAllDepartments();
        return ResponseUtil.success(list.toString());
    }
}
