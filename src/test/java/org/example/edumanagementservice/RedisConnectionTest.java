package org.example.edumanagementservice;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisConnectionTest {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testSetAndGet() {
        RBucket<String> bucket = redissonClient.getBucket("test_key");
        bucket.set("Hello from Redis Cloud");
        String value = bucket.get();
        System.out.println("Redis 读取值: " + value);
    }
}
