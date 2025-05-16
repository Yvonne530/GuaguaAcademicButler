package org.example.edumanagementservice.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendCourseCreatedAlert(Long courseId) {
        System.out.println("通知：课程创建成功，课程ID = " + courseId);
        // 可扩展为邮件、站内信、消息队列等通知方式
    }

    public void sendCourseUpdatedAlert(Long courseId) {
        System.out.println("通知：课程更新成功，课程ID = " + courseId);
    }
}
