package org.example.edumanagementservice.dto;

import lombok.Data;

@Data
public class GradeDTO {
    private String studentId;
    private String courseCode;
    private String semester;
    private Double score;
}
