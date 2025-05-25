package org.example.edumanagementservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "v_student_scores")
@Immutable // 标记为只读，避免 Hibernate 尝试更新视图
public class StudentScoreView {

    @Id
    private Long id; // 注意：视图中需要人工构造该字段，建议用 ROW_NUMBER() 或任意唯一值

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "course_name")
    private String courseName;
    @Column(name = "score")
    private BigDecimal score;

    private String grade;
}
