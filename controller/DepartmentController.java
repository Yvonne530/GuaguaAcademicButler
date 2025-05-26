package org.example.edumanagementservice.controller;

import org.example.edumanagementservice.dto.DepartmentDTO;
import org.example.edumanagementservice.service.DepartmentService;
import org.example.edumanagementservice.util.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Department Controller", description = "院系管理接口")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentDTO dto) {
        return ResponseUtil.created(departmentService.createDepartment(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO dto) {
        return ResponseUtil.ok(departmentService.updateDepartment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseUtil.ok("删除成功");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartment(@PathVariable Long id) {
        return ResponseUtil.ok(departmentService.getDepartmentById(id));
    }

    @GetMapping
    public ResponseEntity<?> listDepartments() {
        List<DepartmentDTO> list = departmentService.getAllDepartments();
        return ResponseUtil.ok(list);
    }
}
