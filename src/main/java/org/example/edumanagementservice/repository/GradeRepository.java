package org.example.edumanagementservice.repository;
@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(String studentId);
    List<Grade> findByCourseCode(String courseCode);
}
