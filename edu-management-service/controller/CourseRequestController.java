package org.example.edumanagementservice.controller;

import org.example.edumanagementservice.dto.CourseRequestDTO;
import org.example.edumanagementservice.model.CourseRequest;
import org.example.edumanagementservice.model.RequestType;
import org.example.edumanagementservice.model.RequestStatus;
import org.example.edumanagementservice.service.CourseRequestService;
import org.example.edumanagementservice.util.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-requests")
@RequiredArgsConstructor
@Tag(name = "课程状态申请", description = "学生提交课程开设/关闭申请，管理员审批")
public class CourseRequestController {

    private final CourseRequestService courseRequestService;

    @PostMapping
    public ResponseEntity<?> submit(@RequestBody CourseRequestDTO dto) {
        return ResponseUtil.created(courseRequestService.submitRequest(dto));
    }

    @PostMapping("/review/{id}")
    public ResponseEntity<?> review(@PathVariable Long id,
                                    @RequestParam boolean approve,
                                    @RequestParam String reviewer) {
        return ResponseUtil.ok(courseRequestService.reviewRequest(id, approve, reviewer));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> listByStudent(@PathVariable Long studentId) {
        return ResponseUtil.ok(courseRequestService.getRequestsByStudent(studentId));
    }

    @PostMapping("/status-change")
    public ResponseEntity<?> submitStatusChange(@RequestBody CourseRequestDTO request) {
        CourseRequest cr = new CourseRequest();
        cr.setCourseId(request.getCourseId());
        cr.setRequestType(RequestType.CHANGE_STATUS);
        cr.setStatus(RequestStatus.PENDING);
        cr.setReason(request.getStatusChangeReason());
        return ResponseUtil.created(courseRequestService.save(cr));
    }

    @GetMapping("/pending")
    public ResponseEntity<?> pending() {
        List<CourseRequestDTO> pendingRequests = courseRequestService.getPendingRequests();
        return ResponseUtil.ok(pendingRequests);
    }
}
