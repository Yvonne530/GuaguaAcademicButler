package org.example.edumanagementservice.service;

import org.example.edumanagementservice.dto.CourseRequestDTO;
import org.example.edumanagementservice.model.CourseRequest;

import java.util.List;

public interface CourseRequestService {
    CourseRequestDTO submitRequest(CourseRequestDTO dto);
    CourseRequestDTO reviewRequest(Long id, boolean approve, String reviewer);

    // 修复返回类型
    List<CourseRequestDTO> getRequestsByStudent(Long studentId);

    List<CourseRequestDTO> getPendingRequests();

    // 类型建议明确为 CourseRequest
    CourseRequest save(CourseRequest cr);
}
