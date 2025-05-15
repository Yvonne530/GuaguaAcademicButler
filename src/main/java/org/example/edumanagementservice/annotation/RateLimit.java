package org.example.edumanagementservice.annotation;



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 注解可以应用于方法
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时可用
public @interface RateLimit {
    String key();
    int value();
}
