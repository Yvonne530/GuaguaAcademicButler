package org.example.edumanagementservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("呱呱学术管家 API 文档")
                        .version("v1.0")
                        .description("用于学生、教师、管理员的后端接口")
                        .contact(new Contact()
                                .name("开发者小爪")
                                .email("xiao.zhua@example.com")));
    }
}
