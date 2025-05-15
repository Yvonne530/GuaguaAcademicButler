package org.example.edumanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.GradeDTO;
import org.example.edumanagementservice.dto.GradeWeightDTO;
import org.example.edumanagementservice.exception.BusinessException;
import org.example.edumanagementservice.model.Grade;
import org.example.edumanagementservice.model.GradeWeight;
import org.example.edumanagementservice.repository.GradeRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
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

        // 2. 验证权重总和=100%
        BigDecimal total = weights.stream()
                .map(dto -> BigDecimal.valueOf(dto.getWeight()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (total.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new BusinessException(400, "权重总和必须等于100%");
        }

        // 3. 更新配置
        weightRepo.deleteByCourseId(courseId);
        List<GradeWeight> entities = weights.stream()
                .map(dto -> new GradeWeight(null, course, dto.getAssessmentType(), dto.getWeight(), teacherId, null))
                .toList();
        weightRepo.saveAll(entities);
    }

    public List<GradeWeightDTO> getGradeWeights(Long courseId) {
        return weightRepo.findByCourseId(courseId)
                .stream()
                .map(w -> new GradeWeightDTO(w.getAssessmentType(), w.getWeight()))
                .toList();
    }

    @Cacheable(value = "studentGrades", key = "#studentId")
    public List<Grade> getGrades(String studentId) {
        return gradeRepository.findByStudentId(studentId);
    }
}
