package org.example.edumanagementservice.controller;

import org.example.edumanagementservice.model.StudentScoreView;
import org.example.edumanagementservice.repository.ViewScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/score")
@RequiredArgsConstructor
public class ScoreController {

    private final ViewScoreRepository viewScoreRepository;

    // 查询指定学生 ID 的所有成绩
    @GetMapping("/student/{studentId}")
    public List<StudentScoreView> getScoresByStudentId(@PathVariable Long studentId) {
        return viewScoreRepository.findByStudentId(studentId);
    }
}
