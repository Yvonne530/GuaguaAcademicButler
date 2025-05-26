package org.example.edumanagementservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long courseId;

    private Long studentId;

    @Enumerated(EnumType.STRING)
    private RequestType requestType; // 开设或关闭等

    @Enumerated(EnumType.STRING)
    private RequestStatus status; // PENDING, APPROVED, REJECTED

    private String reason;

    private LocalDateTime createdAt;

    private LocalDateTime reviewedAt;

    private String reviewer; // 管理员用户名

}
