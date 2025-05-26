package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.CourseRequest;
import org.example.edumanagementservice.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRequestRepository extends JpaRepository<CourseRequest, Long> {
    List<CourseRequest> findByStudentId(Long studentId);
    List<CourseRequest> findByStatus(RequestStatus status);

    CourseRequest save(CourseRequest request);
}
