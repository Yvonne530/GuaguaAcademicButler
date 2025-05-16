package org.example.edumanagementservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.CourseStatsDTO;
import org.example.edumanagementservice.model.Course;
import org.example.edumanagementservice.repository.CourseRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    @Cacheable(value = "courses", key = "#courseId")
    public Course getCourse(String courseId) {
        return courseRepository.findById(Long.valueOf(courseId))
                .orElseThrow(() -> new IllegalArgumentException("课程 ID 不存在：" + courseId));

    }

    @CacheEvict(value = "courses", key = "#course.id")
    public void updateCourse(Course course) {
        courseRepository.save(course);
    }



    @Transactional
    public CourseStatsDTO getCourseStats(String courseId) {
        Map<String, Object> result = courseRepository.callCalculateStats(courseId);
        return new CourseStatsDTO(
                (String) result.get("course_name"),
                ((Number) result.get("student_count")).intValue(),
                ((Number) result.get("avg_score")).doubleValue()
        );
    }
}
