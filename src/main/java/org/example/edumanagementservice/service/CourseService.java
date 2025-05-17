package org.example.edumanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.model.Course;
import org.example.edumanagementservice.model.CourseRequest;
import org.example.edumanagementservice.model.RequestStatus;
import org.example.edumanagementservice.repository.CourseRepository;
import org.example.edumanagementservice.repository.CourseRequestRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseRequestRepository courseRequestRepository;

    @Cacheable(value = "courses", key = "#courseId")
    public Course getCourse(String courseId) {
        return courseRepository.findById(Long.valueOf(courseId))
                .orElseThrow(() -> new IllegalArgumentException("课程 ID 不存在：" + courseId));
    }

    @CacheEvict(value = "courses", key = "#course.id")
    public void updateCourse(Course course) {
        courseRepository.save(course);
    }

    public void approveRequest(Long requestId, Boolean approved, String adminUsername) {
        CourseRequest request = courseRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("找不到课程申请，ID=" + requestId));

        request.setStatus(approved ? RequestStatus.APPROVED : RequestStatus.REJECTED);
        request.setReviewer(adminUsername);
        request.setReviewedAt(LocalDateTime.now());

        courseRequestRepository.save(request);
    }
}
