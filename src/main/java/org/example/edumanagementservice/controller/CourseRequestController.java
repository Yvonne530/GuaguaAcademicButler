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

@RequestMapping("/api/course-requests")
@RequiredArgsConstructor
@Tag(name = "课程状态申请", description = "学生提交课程开设/关闭申请，管理员审批")
public class CourseRequestController {

    private final CourseRequestService courseRequestService;

    @PostMapping
    public Object submit(@RequestBody CourseRequestDTO dto) {
        return ResponseUtil.success("请求提交成功", courseRequestService.submitRequest(dto));
    }

    @PostMapping("/review/{id}")
    public Object review(@PathVariable Long id,
                         @RequestParam boolean approve,
                         @RequestParam String reviewer) {
        return ResponseUtil.success("审核成功", courseRequestService.reviewRequest(id, approve, reviewer));
    }

    @GetMapping("/student/{studentId}")
    public Object listByStudent(@PathVariable Long studentId) {
        String requests = courseRequestService.getRequestsByStudent(studentId);
        return ResponseUtil.success("获取学生请求成功", requests); // 传入消息和请求列表
    }

    @PostMapping("/status-change")
    public ResponseEntity<?> submitStatusChange(@RequestBody CourseRequestDTO request) {
        CourseRequest cr = new CourseRequest();
        cr.setCourseId(request.getCourseId());
        cr.setRequestType(RequestType.CHANGE_STATUS);
        cr.setStatus(RequestStatus.PENDING);
        cr.setReason(request.getStatusChangeReason()); // 使用新增的字段
        return ResponseEntity.ok(courseRequestService.save(cr));
    }

    @GetMapping("/pending")
    public Object pending() {
        List<CourseRequestDTO> pendingRequests = courseRequestService.getPendingRequests();
        return ResponseUtil.success("获取待处理请求成功", pendingRequests); // 传入消息和待处理请求列表
    }
}
