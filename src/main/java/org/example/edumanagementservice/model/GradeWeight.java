package org.example.edumanagementservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "grade_weight")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeWeight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 50)
    private String itemName; // 如："作业"、"期中考试"、"期末考试"

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal weight; // 0.30 表示30%

    @Column(name = "created_by", nullable = false)
    private Long teacherId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}