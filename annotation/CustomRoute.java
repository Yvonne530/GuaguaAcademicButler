package org.example.edumanagementservice.annotation;

import org.springframework.web.bind.annotation.RequestMethod;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomRoute {
    String value(); // 路径
    RequestMethod method() default RequestMethod.GET;
}