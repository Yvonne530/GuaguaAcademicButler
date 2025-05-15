package org.example.edumanagementservice.repository;

package com.edu.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface EnhancedJpaRepository<T, ID> extends JpaRepository<T, ID> {

    /**
     * 动态条件查询（安全提示：需防范SQL注入）
     * @param whereClause WHERE后的条件语句（如 "name='张三' AND age>20"）
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM #{#entityName} WHERE ${jpqlWhere}")
    List<T> dynamicQuery(@Param("jpqlWhere") String whereClause);

    /**
     * 调用存储过程归档旧数据
     * @param cutoffDate 截止日期
     * @return 归档记录数
     */
    @Procedure(procedureName = "sp_archive_old_data")
    int archiveOldData(@Param("cutoff_date") LocalDate cutoffDate);
}