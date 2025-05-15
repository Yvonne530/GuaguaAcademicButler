package org.example.edumanagementservice.service;

import org.example.edumanagementservice.dto.CourseRequestDTO;
import org.example.edumanagementservice.model.CourseRequest;

import java.util.List;

public interface CourseRequestService {
    CourseRequestDTO submitRequest(CourseRequestDTO dto);
    CourseRequestDTO reviewRequest(Long id, boolean approve, String reviewer);
    String getRequestsByStudent(Long studentId);
    List<CourseRequestDTO> getPendingRequests();

    Object save(CourseRequest cr);
}
