package org.example.edumanagementservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.CourseStatsDTO;
import org.example.edumanagementservice.model.Course; // 确保导入 Course 模型
import org.example.edumanagementservice.repository.CourseRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository; // 使用统一的 CourseRepository

    @Cacheable(value = "courses", key = "#courseId")
    public Course getCourse(String courseId) {
        return courseRepository.findById(courseId).orElseThrow();
    }

    @CacheEvict(value = "courses", key = "#course.id")
    public void updateCourse(Course course) {
        courseRepository.save(course);
    }

    // 新增审批方法
    @Transactional
    public static void approveRequest(Long id, Boolean approved, String approver) {
        courseRepository.findById(id).ifPresent(course -> {
            course.setStatus(approved ? "APPROVED" : "REJECTED");
            course.setApprover(approver); // 确保 Course 实体类有 approver 字段
            courseRepository.save(course);
        });
    }

    // 原有统计方法
    @Transactional(readOnly = true)
    public CourseStatsDTO getCourseStats(String courseId) {
        Map<String, Object> result = courseRepository.callCalculateStats(courseId);
        return new CourseStatsDTO(
                (String) result.get("course_name"),
                ((Number) result.get("student_count")).intValue(),
                ((Number) result.get("avg_score")).doubleValue()
        );
    }
}
