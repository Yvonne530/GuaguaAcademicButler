package org.example.edumanagementservice.strategy;

import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 学生权限策略实现
 */
@Component("studentPermissionStrategy")
public class StudentPermissionStrategy implements PermissionStrategy {

    private static final Set<String> ALLOWED = Set.of(
            "COURSE_VIEW", "COURSE_ENROLL", "SCHEDULE_VIEW", "INFO_UPDATE"
    );

    @Override
    public boolean hasPermission(String permissionKey) {
        return ALLOWED.contains(permissionKey);
    }
}
