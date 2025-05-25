package org.example.edumanagementservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity

@Table(name = "course_selection")


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSelection {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "student_id")
    private Long studentId;
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    private String semester;
}
