package org.example.edumanagementservice.model.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreRemove;
import org.example.edumanagementservice.model.Course;
import org.example.edumanagementservice.service.NotificationService;
import org.example.edumanagementservice.util.ApplicationContextHolder;

// 文件位置：src/main/java/com/edu/management/model/listener/CourseUpdateListener.java
public class CourseUpdateListener {

    private final NotificationService notificationService;

    // 构造器注入（需配合@Configurable）
    public CourseUpdateListener() {
        this.notificationService = ApplicationContextHolder.getBean(NotificationService.class);
    }

    @PostPersist
    public void afterCourseCreate(Course course) {
        notificationService.sendCourseCreatedAlert(course.getId());
    }

    @PostUpdate
    public void afterCourseUpdate(Course course) {
        notificationService.sendCourseUpdatedAlert(course.getId());
    }

    @PreRemove
    public void beforeCourseDelete(Course course) {
        // 删除前校验
        if (course.getStatus().equals("PUBLISHED")) {
            throw new IllegalStateException("已发布的课程不能删除");
        }
    }
}