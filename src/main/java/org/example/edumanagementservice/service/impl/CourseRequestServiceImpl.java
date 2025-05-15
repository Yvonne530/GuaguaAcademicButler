package org.example.edumanagementservice.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.CourseRequestDTO;
import org.example.edumanagementservice.exception.CustomException;
import org.example.edumanagementservice.model.CourseRequest;
import org.example.edumanagementservice.model.RequestStatus;
import org.example.edumanagementservice.repository.CourseRequestRepository;
import org.example.edumanagementservice.service.CourseRequestService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseRequestServiceImpl implements CourseRequestService {

    private final CourseRequestRepository courseRequestRepository;

    @Override
    public CourseRequestDTO submitRequest(CourseRequestDTO dto) {
        CourseRequest request = CourseRequest.builder()
                .courseId(dto.getCourseId())
                .studentId(dto.getStudentId())
                .requestType(dto.getRequestType())
                .status(RequestStatus.PENDING)
                .reason(dto.getReason())
                .createdAt(LocalDateTime.now())
                .build();
        return convertToDTO(courseRequestRepository.save(request));
    }

    @Override
    public CourseRequestDTO reviewRequest(Long id, boolean approve, String reviewer) {
        CourseRequest request = courseRequestRepository.findById(id)
                .orElseThrow(() -> new CustomException("未找到课程申请ID: " + id));
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new CustomException("该申请已审核，不能重复操作");
        }

        request.setStatus(approve ? RequestStatus.APPROVED : RequestStatus.REJECTED);
        request.setReviewedAt(LocalDateTime.now());
        request.setReviewer(reviewer);
        return convertToDTO(courseRequestRepository.save(request));
    }

    @Override
    public String getRequestsByStudent(Long studentId) {
        return courseRequestRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseRequestDTO> getPendingRequests() {
        return courseRequestRepository.findByStatus(RequestStatus.PENDING).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CourseRequestDTO convertToDTO(CourseRequest entity) {
        return CourseRequestDTO.builder()
                .id(entity.getId())
                .courseId(entity.getCourseId())
                .studentId(entity.getStudentId())
                .requestType(entity.getRequestType())
                .status(entity.getStatus())
                .reason(entity.getReason())
                .createdAt(entity.getCreatedAt())
                .reviewedAt(entity.getReviewedAt())
                .reviewer(entity.getReviewer())
                .build();
    }
}
