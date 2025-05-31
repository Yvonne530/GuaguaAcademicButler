package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByIdAndTeacherId(Long courseId, Long teacherId);

    // ✅ 新增：用于通过 courseCode 查找课程（用于选课逻辑）
    Optional<Course> findByCourseCode(String courseCode);
}
