package org.example.edumanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// 文件位置：src/main/java/com/edu/management/model/dto/CourseStatsDTO.java
@Data
@AllArgsConstructor
public class CourseStatsDTO {
    private String courseName;
    private int studentCount;
    private double avgScore;
}