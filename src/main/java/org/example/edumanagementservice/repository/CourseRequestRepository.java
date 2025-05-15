package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.CourseRequest;
import org.example.edumanagementservice.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CourseRequestRepository extends JpaRepository<CourseRequest, Long> {
    List<CourseRequest> findByStudentId(Long studentId);
    List<CourseRequest> findByStatus(RequestStatus status);
    // CourseRequestController.java
    @PostMapping("/status-change")
    public ResponseEntity<?> submitStatusChange(
            @RequestBody CourseStatusChangeRequest request) {
        CourseRequest cr = new CourseRequest();
        cr.setCourseId(request.getCourseId());
        cr.setRequestType(RequestType.CHANGE_STATUS);
        cr.setStatus(RequestStatus.PENDING);
        cr.setReason(request.getReason());
        return ResponseEntity.ok(courseRequestService.save(cr));
    }
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Course c WHERE c.id = :courseId AND c.teacher.id = :teacherId")
    boolean existsByIdAndTeacherId(@Param("courseId") Long courseId,
                                   @Param("teacherId") Long teacherId);
}
