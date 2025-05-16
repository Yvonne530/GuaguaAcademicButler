package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.GradeWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeWeightRepository extends JpaRepository<GradeWeight, Long> {
    List<GradeWeight> findByCourseId(Long courseId);
    void deleteByCourseId(Long courseId);
}
