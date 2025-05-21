package org.example.edumanagementservice.strategy;

import org.springframework.stereotype.Component;

/**
 * 管理员权限策略实现：默认拥有所有权限
 */
@Component("adminPermissionStrategy")
public class AdminPermissionStrategy implements PermissionStrategy {

}
