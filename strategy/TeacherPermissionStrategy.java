package org.example.edumanagementservice.strategy;


import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 教师权限策略实现
 */
@Component("teacherPermissionStrategy")
public class TeacherPermissionStrategy implements PermissionStrategy {

    private static final Set<String> ALLOWED = Set.of(
            "COURSE_VIEW", "COURSE_EDIT", "COURSE_PUBLISH", "STUDENT_LIST"
    );

}
