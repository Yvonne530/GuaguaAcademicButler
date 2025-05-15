package org.example.edumanagementservice;


import org.example.edumanagementservice.dto.CourseRequestDTO;
import org.example.edumanagementservice.model.RequestStatus;
import org.example.edumanagementservice.model.RequestType;
import org.example.edumanagementservice.service.CourseRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourseRequestServiceTests {

    @Autowired
    private CourseRequestService courseRequestService;

    private CourseRequestDTO sampleRequest;

    @BeforeEach
    public void setup() {
        sampleRequest = CourseRequestDTO.builder()
                .courseId(1L)
                .studentId(1001L)
                .requestType(RequestType.OPEN_COURSE)
                .reason("希望开设此课程")
                .build();
    }

    @Test
    public void testSubmitRequest() {
        CourseRequestDTO result = courseRequestService.submitRequest(sampleRequest);
        assertNotNull(result.getId());
        assertEquals(RequestStatus.PENDING, result.getStatus());
        assertEquals("希望开设此课程", result.getReason());
    }

    @Test
    public void testReviewRequest() {
        Cour-- ----------------------------
                -- Table structure for course_requests
                -- ----------------------------
        CREATE TABLE course_requests (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                course_id BIGINT NOT NULL,
                student_id BIGINT NOT NULL,
                request_type VARCHAR(50) NOT NULL, -- OPEN_COURSE, CLOSE_COURSE, MODIFY_COURSE
                reason TEXT,
                status VARCHAR(50) DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
        submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        reviewed_at TIMESTAMP,
        reviewer VARCHAR(100),

                CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES course(id),
                CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES student(id)
);
        seRequestDTO submitted = courseRequestService.submitRequest(sampleRequest);
        CourseRequestDTO reviewed = courseRequestService.reviewRequest(submitted.getId(), true, "adminUser");
        assertEquals(RequestStatus.APPROVED, reviewed.getStatus());
        assertEquals("adminUser", reviewed.getReviewer());
        assertNotNull(reviewed.getReviewedAt());
    }

    @Test
    public void testGetRequestsByStudent() {
        courseRequestService.submitRequest(sampleRequest);
        List<CourseRequestDTO> list = courseRequestService.getRequestsByStudent(1001L);
        assertFalse(list.isEmpty());
        assertEquals(1001L, list.get(0).getStudentId());
    }

    @Test
    public void testGetPendingRequests() {
        courseRequestService.submitRequest(sampleRequest);
        List<CourseRequestDTO> list = courseRequestService.getPendingRequests();
        assertTrue(list.stream().anyMatch(req -> req.getStatus() == RequestStatus.PENDING));
    }
}
