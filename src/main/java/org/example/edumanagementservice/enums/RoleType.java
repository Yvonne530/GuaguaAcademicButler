package org.example.edumanagementservice.enums;

public enum RoleType {

    ADMIN("ROLE_ADMIN"),
    TEACHER("ROLE_TEACHER"),
    STUDENT("ROLE_STUDENT");

    private final String authority;

    RoleType(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    /**
     * 从JWT claims中解析角色（兼容两种格式）
     */
    public static RoleType fromAuthority(Object claimValue) {
        if (claimValue == null) {
            throw new IllegalArgumentException("角色声明不存在");
        }

        String roleStr = claimValue.toString().toUpperCase();

        for (RoleType role : values()) {
            if (roleStr.equals(role.name()) ||
                    roleStr.equals(role.getAuthority().toUpperCase())) {
                return role;
            }
        }

        throw new IllegalArgumentException("无效的角色声明: " + claimValue);
    }
    public static RoleType fromUserType(String userType) {
        if (userType == null || userType.trim().isEmpty()) {
            throw new IllegalArgumentException("用户类型不能为空");
        }
        switch (userType.toLowerCase()) {
            case "admin":
                return ADMIN;
            case "teacher":
                return TEACHER;
            case "student":
                return STUDENT;
            default:
                throw new IllegalArgumentException("未知的用户类型：" + userType);
        }
    }


}
