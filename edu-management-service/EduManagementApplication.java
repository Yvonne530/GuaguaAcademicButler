package org.example.edumanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableCaching  // 启用缓存（配合CacheConfig使用）
@EnableScheduling // 启用定时任务（可选）
public class EduManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduManagementApplication.class, args);
    }
}