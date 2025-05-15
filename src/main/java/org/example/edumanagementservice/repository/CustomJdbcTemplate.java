package org.example.edumanagementservice.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class CustomJdbcTemplate {
    private final JdbcTemplate jdbc;

    // 构造器注入DataSource
    public CustomJdbcTemplate(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
        this.jdbc.setFetchSize(100); // 优化批量查询
    }

    /**
     * 通用对象映射查询
     * @param sql SQL语句
     * @param mapper 行映射器
     * @param args 参数
     * @return 实体列表
     */
    public <T> List<T> query(String sql, RowMapper<T> mapper, Object... args) {
        return jdbc.query(sql, args, mapper);
    }

    /**
     * 返回Map列表（适用于动态字段）
     */
    public List<Map<String, Object>> queryForMapList(String sql, Object... args) {
        return jdbc.queryForList(sql, args);
    }

    /**
     * 批量操作（性能优化）
     */
    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        return jdbc.batchUpdate(sql, batchArgs);
    }
}