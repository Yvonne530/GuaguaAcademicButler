package org.example.edumanagementservice.strategy;

public interface PermissionStrategy {

    /**
     * 判断当前用户是否有指定操作权限
     * @param permissionKey 权限标识（如 "COURSE_EDIT"）
     * @return 是否有权限
     */
    boolean hasPermission(String permissionKey);
}
