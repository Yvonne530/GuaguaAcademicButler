package org.example.edumanagementservice.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;

@Repository
public class CustomJdbcTemplate {
    private final JdbcTemplate jdbc;

    // 构造器注入DataSource
    public CustomJdbcTemplate(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
        this.jdbc.setFetchSize(100); // 优化批量查询
    }

}