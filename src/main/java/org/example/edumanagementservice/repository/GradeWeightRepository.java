package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.GradeWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeWeightRepository extends JpaRepository<GradeWeight, Long> {
    @Modifying
    @Query("delete from GradeWeight w where w.course.id = :courseId")
    void deleteByCourseId(@Param("courseId") Long courseId);
}
