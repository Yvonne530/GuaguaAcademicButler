package org.example.edumanagementservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewScoreRepository extends JpaRepository<StudentScoreEntity, Integer> {
    @Query(value = "SELECT * FROM v_student_scores WHERE student_id = :studentId", nativeQuery = true)
    List<StudentScoreView> findByStudentId(@Param("studentId") Integer studentId);
}
