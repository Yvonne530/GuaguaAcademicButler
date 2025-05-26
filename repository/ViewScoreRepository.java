package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.StudentScoreView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryDefinition(domainClass = StudentScoreView.class, idClass = Long.class)
public interface ViewScoreRepository {
    @Query(value = "SELECT * FROM v_student_scores WHERE student_id = :studentId", nativeQuery = true)
    List<StudentScoreView> findByStudentId(@Param("studentId") Long studentId);
}
