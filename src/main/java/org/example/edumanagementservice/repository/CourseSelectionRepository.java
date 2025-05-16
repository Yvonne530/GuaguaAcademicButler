package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.CourseSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSelectionRepository extends JpaRepository<CourseSelection, Long> {
    List<CourseSelection> findByStudentId(String studentId);
    boolean existsByStudentIdAndCourseCode(String studentId, String courseCode);
}
