package org.example.edumanagementservice.model;

import lombok.Data;
import org.example.edumanagementservice.enums.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * JWT用户认证实体，实现Spring Security的UserDetails接口
 */
@Data
public class JwtUser implements UserDetails {

    private Long id;          // 新增：用户唯一ID（如数据库主键）
    private String username; // 用户名（如学号/工号）
    private String password; // 密码（加密存储）
    private RoleType roleType; // 角色类型（如 STUDENT, TEACHER, ADMIN）

    // Spring Security 相关状态字段
    private boolean accountNonExpired = true;      // 账户是否未过期
    private boolean accountNonLocked = true;       // 账户是否未锁定
    private boolean credentialsNonExpired = true;  // 凭证是否未过期
    private boolean enabled = true;                // 账户是否启用

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 返回用户的权限（基于角色）
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + roleType.name())
        );
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
