package org.example.edumanagementservice.dto;

import org.example.edumanagementservice.model.RequestStatus;
import org.example.edumanagementservice.model.RequestType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CourseRequestDTO {
    private Long id;
    private Long courseId;
    private Long studentId;
    private RequestType requestType;
    private RequestStatus status;
    private String reason;
    private String reviewer;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;

    private String statusChangeReason;
}
