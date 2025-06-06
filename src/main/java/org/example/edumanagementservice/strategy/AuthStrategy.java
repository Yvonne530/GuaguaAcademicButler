package org.example.edumanagementservice.strategy;

public interface AuthStrategy {
    /**
     * 登录接口，返回生成的 JWT Token
     *
     * @param username 用户名
     * @param password 密码
     * @return JWT Token
     */
    String login(String username, String password);
}
