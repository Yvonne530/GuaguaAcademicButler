package org.example.edumanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.model.CourseRequest;
import org.example.edumanagementservice.model.RequestStatus;
import org.example.edumanagementservice.repository.CourseRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRequestRepository courseRequestRepository;

    public void approveRequest(Long requestId, Boolean approved, String adminUsername) {
        CourseRequest request = courseRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("找不到课程申请，ID=" + requestId));

        request.setStatus(approved ? RequestStatus.APPROVED : RequestStatus.REJECTED);
        request.setReviewer(adminUsername);
        request.setReviewedAt(LocalDateTime.now());

        courseRequestRepository.save(request);
    }
}
