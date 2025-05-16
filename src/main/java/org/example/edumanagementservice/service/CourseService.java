package org.example.edumanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.model.Course;
import org.example.edumanagementservice.repository.CourseRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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

}
