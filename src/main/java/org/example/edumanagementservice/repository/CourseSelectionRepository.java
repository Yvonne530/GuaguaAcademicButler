package org.example.edumanagementservice.repository;

@Repository
public interface CourseSelectionRepository extends JpaRepository<CourseSelection, Long> {
    List<CourseSelection> findByStudentId(String studentId);
    boolean existsByStudentIdAndCourseCode(String studentId, String courseCode);
}
