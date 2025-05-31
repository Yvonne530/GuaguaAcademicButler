package org.example.edumanagementservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.CourseSelectionDTO;
import org.example.edumanagementservice.exception.BusyOperationException;
import org.example.edumanagementservice.model.Course;
import org.example.edumanagementservice.model.CourseSelection;
import org.example.edumanagementservice.repository.CourseRepository;
import org.example.edumanagementservice.repository.CourseSelectionRepository;
import org.example.edumanagementservice.service.lock.DistributedLockService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CourseSelectionService {

    private final CourseSelectionRepository repository;
    private final CourseRepository courseRepository;
    private final DistributedLockService lockService;

    public void selectCourse(Long studentId, CourseSelectionDTO dto) {
        selectCourseWithLock(studentId, dto); // 使用加锁版本确保并发安全
    }

    @Transactional
    public void selectCourseWithLock(Long studentId, CourseSelectionDTO dto) {
        String lockKey = "selection:" + dto.getCourseCode();
        try {
            if (!lockService.tryLock(lockKey, 5, 10, TimeUnit.SECONDS)) {
                throw new BusyOperationException("选课人数过多，请稍后重试");
            }

            if (repository.existsByStudentIdAndCourse_CourseCode(studentId, dto.getCourseCode())) {
                throw new IllegalArgumentException("课程已选择");
            }

            Course course = courseRepository.findByCourseCode(dto.getCourseCode())
                    .orElseThrow(() -> new IllegalArgumentException("课程不存在"));

            CourseSelection cs = new CourseSelection(null, studentId, course, dto.getSemester());
            repository.save(cs);
        } finally {
            lockService.unlock(lockKey);
        }
    }

    public List<CourseSelection> getSchedule(Long studentId) {
        return repository.findByStudentId(studentId);
    }
}
