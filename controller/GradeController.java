package org.example.edumanagementservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.GradeDTO;
import org.example.edumanagementservice.dto.GradeWeightDTO;
import org.example.edumanagementservice.model.JwtUser;
import org.example.edumanagementservice.service.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grade")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitGrade(@RequestBody GradeDTO dto) {
        gradeService.submitGrade(dto);
        return ResponseEntity.ok("成绩提交成功");
    }

    @Operation(summary = "设置分数比重", description = "需满足权重总和=100%")
    @PostMapping("/weights/{courseId}")
    public ResponseEntity<?> setWeights(
            @PathVariable Long courseId,
            @AuthenticationPrincipal JwtUser teacher,
            @Valid @RequestBody List<GradeWeightDTO> weights) { // ✅ 使用 GradeWeightDTO
        gradeService.setGradeWeights(courseId, teacher.getId(), weights);
        return ResponseEntity.ok("权重设置成功");
    }

    @GetMapping("/view")
    public ResponseEntity<?> viewGrades(@AuthenticationPrincipal JwtUser user) {
        return ResponseEntity.ok(gradeService.getGrades(user.getUsername()));
    }
}
