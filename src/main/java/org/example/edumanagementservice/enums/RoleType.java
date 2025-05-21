package org.example.edumanagementservice.enums;

import lombok.Getter;

@Getter
public enum RoleType {

    ADMIN("ROLE_ADMIN"),
    TEACHER("ROLE_TEACHER"),
    STUDENT("ROLE_STUDENT");

    private final String authority;

    RoleType(String authority) {
        this.authority = authority;
    }

    public static RoleType fromUserType(String userType) {
        if (userType == null || userType.trim().isEmpty()) {
            throw new IllegalArgumentException("用户类型不能为空");
        }
        return switch (userType.toLowerCase()) {
            case "admin" -> ADMIN;
            case "teacher" -> TEACHER;
            case "student" -> STUDENT;
            default -> throw new IllegalArgumentException("未知的用户类型：" + userType);
        };
    }


}
