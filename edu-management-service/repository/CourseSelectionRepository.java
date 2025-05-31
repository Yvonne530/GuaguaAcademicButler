package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.CourseSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSelectionRepository extends JpaRepository<CourseSelection, Long> {

    List<CourseSelection> findByStudentId(Long studentId);

    // 正确嵌套字段写法，代表：CourseSelection.course.code
    boolean existsByStudentIdAndCourse_CourseCode(Long studentId, String code);
}
