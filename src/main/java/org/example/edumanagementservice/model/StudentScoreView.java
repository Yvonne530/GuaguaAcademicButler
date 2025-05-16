package org.example.edumanagementservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "v_student_scores")
public class StudentScoreView {

    @Id
    private Integer id; // 视图必须有主键，哪怕是重复的字段

    private Integer studentId;
    private String studentName;
    private String courseName;
    private Double score;
    private String grade;
}
