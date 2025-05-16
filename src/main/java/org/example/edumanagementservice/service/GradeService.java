package org.example.edumanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.GradeDTO;
import org.example.edumanagementservice.dto.GradeWeightDTO;
import org.example.edumanagementservice.exception.BusinessException;
import org.example.edumanagementservice.model.Course;
import org.example.edumanagementservice.model.Grade;
import org.example.edumanagementservice.model.GradeWeight;
import org.example.edumanagementservice.repository.CourseRepository;
import org.example.edumanagementservice.repository.GradeRepository;
import org.example.edumanagementservice.repository.GradeWeightRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final CourseRepository courseRepo;
    private final GradeWeightRepository weightRepo;

    public void submitGrade(GradeDTO dto) {
        Grade grade = new Grade(null, dto.getStudentId(), dto.getCourseCode(), dto.getSemester(), dto.getScore());
        gradeRepository.save(grade);
    }

    public void setGradeWeights(Long courseId, Long teacherId, List<GradeWeightDTO> weights) {
        // 1. 验证课程和教师权限
        Course course = courseRepo.findByIdAndTeacherId(courseId, teacherId)
                .orElseThrow(() -> new BusinessException(404, "课程不存在或无权操作"));

        // 2. 验证权重总和 = 100%
        BigDecimal total = weights.stream()
                .map(dto -> BigDecimal.valueOf(dto.getWeight()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new BusinessException(400, "权重总和必须等于100%");
        }

        // 3. 删除旧配置并保存新配置
        weightRepo.deleteByCourseId(courseId);
        List<GradeWeight> entities = weights.stream()
                .map(dto -> new GradeWeight(null, course, dto.getItemName(),
                        BigDecimal.valueOf(dto.getWeight()), teacherId, LocalDateTime.now()))
                .toList();
        weightRepo.saveAll(entities);
    }

    public List<GradeWeightDTO> getGradeWeights(Long courseId) {
        return weightRepo.findByCourseId(courseId)
                .stream()
                .map(w -> new GradeWeightDTO(w.getItemName(), w.getWeight().doubleValue()))
                .toList();
    }

    @Cacheable(value = "studentGrades", key = "#studentId")
    public List<Grade> getGrades(String studentId) {
        return gradeRepository.findByStudentId(studentId);
    }
}
