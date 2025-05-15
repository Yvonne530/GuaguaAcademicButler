package org.example.edumanagementservice.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CachePool {

    private final Map<String, CacheObject> cacheMap = new ConcurrentHashMap<>();

    // 设置缓存（默认5分钟）
    public void put(String key, Object value) {
        put(key, value, 5 * 60 * 1000); // 5分钟
    }

    public void put(String key, Object value, long expireMillis) {
        cacheMap.put(key, new CacheObject(value, System.currentTimeMillis() + expireMillis));
    }

    // 获取缓存
    public Object get(String key) {
        CacheObject cacheObject = cacheMap.get(key);
        if (cacheObject == null || cacheObject.isExpired()) {
            cacheMap.remove(key);
            return null;
        }
        return cacheObject.getValue();
    }

    // 删除缓存
    public void remove(String key) {
        cacheMap.remove(key);
    }

    // 清理所有过期项（可定时调度）
    public void cleanup() {
        long now = System.currentTimeMillis();
        cacheMap.entrySet().removeIf(entry -> entry.getValue().getExpireTime() < now);
    }

    @Data
    @AllArgsConstructor
    private static class CacheObject {
        private Object value;
        private long expireTime;

        boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }
    @Scheduled(fixedRate = 10 * 60 * 1000) // 每10分钟清理一次
    public void scheduledCleanup() {
        cleanup();
    }

}
