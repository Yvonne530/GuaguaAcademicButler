package org.example.edumanagementservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 建议重命名为 courseName（推荐方式）
    private String courseName;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private TeacherPermission teacher;

    private String courseCode;

    private String status;

    private String description;

    private String semester;
}
