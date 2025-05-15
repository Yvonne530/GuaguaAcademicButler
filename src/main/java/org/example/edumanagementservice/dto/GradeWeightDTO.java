package org.example.edumanagementservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class GradeWeightDTO {
    private String assessmentType; // e.g., "作业", "考试", "实验"

    @Min(0)
    @Max(100)
    private int weight; // 权重百分比（0\~100）
}
