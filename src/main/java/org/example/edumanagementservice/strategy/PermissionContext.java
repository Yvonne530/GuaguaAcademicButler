package org.example.edumanagementservice.strategy;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 权限策略上下文，根据用户类型选择对应策略
 */
@Component
public class PermissionContext {

    private final Map<String, PermissionStrategy> strategyMap;

    @Autowired
    public PermissionContext(Map<String, PermissionStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public PermissionStrategy getStrategy(String userType) {
        String key = switch (userType.toLowerCase()) {
            case "admin" -> "adminPermissionStrategy";
            case "teacher" -> "teacherPermissionStrategy";
            case "student" -> "studentPermissionStrategy";
            default -> throw new IllegalArgumentException("不支持的用户类型: " + userType);
        };
        return strategyMap.get(key);
    }
}
