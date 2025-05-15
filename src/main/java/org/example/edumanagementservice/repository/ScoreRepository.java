package org.example.edumanagementservice.repository;

public interface StudentScoreView {
    Integer getStudentId();
    String getStudentName();
    Integer getCourseId();
    String getCourseName();
    Double getScore();
    String getGrade();
    String getTeacherName();
}

@Repository
public interface ViewScoreRepository extends JpaRepository<StudentScoreEntity, Integer> {
    @Query(value = "SELECT * FROM v_student_scores WHERE student_id = :studentId", nativeQuery = true)
    List<StudentScoreView> findByStudentId(@Param("studentId") Integer studentId);
}
