package org.example.edumanagementservice.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private RequestType requestType; // 开设或关闭等

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private RequestStatus status; // PENDING, APPROVED, REJECTED

    private String reason;

    private LocalDateTime createdAt;

    private LocalDateTime reviewedAt;

    private String reviewer; // 管理员用户名

}
