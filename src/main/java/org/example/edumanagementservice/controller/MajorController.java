package org.example.edumanagementservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.MajorDTO;
import org.example.edumanagementservice.service.MajorService;
import org.example.edumanagementservice.util.ResponseUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
@Tag(name = "Major Controller", description = "专业管理接口")
public class MajorController {

    private final MajorService majorService;

    @PostMapping // 创建专业（使用 MajorDTO）
    public Object createMajor(@RequestBody MajorDTO dto) {
        return ResponseUtil.success(String.valueOf(majorService.createMajor(dto)));
    }

    @PutMapping("/{id}") // 更新专业（使用 MajorDTO）
    public Object updateMajor(@PathVariable Long id, @RequestBody MajorDTO dto) {
        return ResponseUtil.success(String.valueOf(majorService.updateMajor(id, dto)));
    }

    @DeleteMapping("/{id}") // 删除专业（使用 Long）
    public Object deleteMajor(@PathVariable Long id) {
        majorService.deleteMajor(id);
        return ResponseUtil.success("删除成功");
    }

    @GetMapping("/{id}") // 获取单个专业信息（使用 Long）
    public Object getMajor(@PathVariable Long id) {
        return ResponseUtil.success(String.valueOf(majorService.getMajorById(id)));
    }

    @GetMapping("/department/{departmentId}") // 按院系获取专业列表（使用 Long）
    public Object getMajorsByDepartment(@PathVariable Long departmentId) {
        return ResponseUtil.success(majorService.getMajorsByDepartment(departmentId).toString());
    }

    @GetMapping // 获取所有专业列表（使用 getAllMajors()）
    public Object listMajors() {
        List<MajorDTO> majors = majorService.getAllMajors();
        return ResponseUtil.success(majors.toString());
    }
}

