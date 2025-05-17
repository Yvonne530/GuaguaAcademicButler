package org.example.edumanagementservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.CourseSelectionDTO;
import org.example.edumanagementservice.exception.BusyOperationException;
import org.example.edumanagementservice.model.CourseSelection;
import org.example.edumanagementservice.repository.CourseSelectionRepository;
import org.example.edumanagementservice.service.lock.DistributedLockService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CourseSelectionService {

    private final CourseSelectionRepository repository;
    private final DistributedLockService lockService;

    // ✅ 新增公开方法，供 Controller 调用
    public void selectCourse(String studentId, CourseSelectionDTO dto) {
        selectCourseWithLock(studentId, dto); // 使用加锁版本确保并发安全
    }

    @Transactional
    public void selectCourseWithLock(String studentId, CourseSelectionDTO dto) {
        String lockKey = "selection:" + dto.getCourseCode();
        try {
            if (!lockService.tryLock(lockKey, 5, 10, TimeUnit.SECONDS)) {
                throw new BusyOperationException("选课人数过多，请稍后重试");
            }

            if (repository.existsByStudentIdAndCourseCode(studentId, dto.getCourseCode())) {
                throw new IllegalArgumentException("课程已选择");
            }

            CourseSelection cs = new CourseSelection(null, studentId, dto.getCourseCode(), dto.getSemester());
            repository.save(cs);
        } finally {
            lockService.unlock(lockKey);
        }
    }

    public List<CourseSelection> getSchedule(String studentId) {
        return repository.findByStudentId(studentId);
    }
}
