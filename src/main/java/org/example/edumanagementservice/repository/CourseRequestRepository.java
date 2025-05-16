package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.CourseRequest;
import org.example.edumanagementservice.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRequestRepository extends JpaRepository<CourseRequest, Long> {
    List<CourseRequest> findByStudentId(Long studentId);
    List<CourseRequest> findByStatus(RequestStatus status);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Course c WHERE c.id = :courseId AND c.teacher.id = :teacherId")
    boolean existsByIdAndTeacherId(@Param("courseId") Long courseId,
                                   @Param("TeacherId") Long teacherId);
}
