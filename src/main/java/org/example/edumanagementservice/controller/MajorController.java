package org.example.edumanagementservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.MajorDTO;
import org.example.edumanagementservice.service.MajorService;
import org.example.edumanagementservice.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
@Tag(name = "Major Controller", description = "专业管理接口")
public class MajorController {

    private final MajorService majorService;

    @PostMapping
    public ResponseEntity<?> createMajor(@Valid @RequestBody MajorDTO dto) {
        MajorDTO created = majorService.createMajor(dto);
        return ResponseUtil.created(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMajor(@PathVariable Long id, @Valid @RequestBody MajorDTO dto) {
        MajorDTO updated = majorService.updateMajor(id, dto);
        return ResponseUtil.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMajor(@PathVariable Long id) {
        majorService.deleteMajor(id);
        return ResponseUtil.ok("删除成功");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMajor(@PathVariable Long id) {
        return ResponseUtil.ok(majorService.getMajorById(id));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<?> getMajorsByDepartment(@PathVariable Long departmentId) {
        return ResponseUtil.ok(majorService.getMajorsByDepartment(departmentId));
    }

    @GetMapping
    public ResponseEntity<?> listMajors() {
        return ResponseUtil.ok(majorService.getAllMajors());
    }
}
