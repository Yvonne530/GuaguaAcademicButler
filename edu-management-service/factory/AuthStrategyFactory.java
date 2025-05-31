package org.example.edumanagementservice.factory;

import org.example.edumanagementservice.strategy.AuthStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthStrategyFactory {

    private final Map<String, AuthStrategy> strategyMap;

    @Autowired
    public AuthStrategyFactory(Map<String, AuthStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    /**
     * 根据用户类型获取对应的登录策略
     * @param userType 用户类型（admin / teacher / student）
     * @return 对应的 AuthStrategy 实现
     */
    public AuthStrategy getStrategy(String userType) {
        String key = switch (userType.toLowerCase()) {
            case "admin" -> "adminAuthStrategy";
            case "teacher" -> "teacherAuthStrategy";
            case "student" -> "studentAuthStrategy";
            default -> throw new IllegalArgumentException("不支持的用户类型: " + userType);
        };
        return strategyMap.get(key);
    }
}
