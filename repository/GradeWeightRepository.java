package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.GradeWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeWeightRepository extends JpaRepository<GradeWeight, Long> {
    void deleteByCourseId(Long courseId);
}
