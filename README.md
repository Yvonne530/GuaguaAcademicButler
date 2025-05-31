# 呱呱学术管家系统 - 后端项目说明文档（完整版）

## 项目说明

- **项目名称**：Edu Management Service（呱呱学术管家）  
- **项目目标**：构建一套面向高校/教育机构的全功能教务管理系统，支持角色权限控制、安全认证、成绩管理、选课管理、账户与数据分析等核心业务，具备高性能、可维护、可扩展的特点。

## 开发环境与技术栈

- **技术**：
  - JDK 17
  - Spring Boot 3.2.0
  - Spring Security 6.2
  - Hibernate / JPA 6.3
  - MySQL 8.x
  - JWT (JJWT) 0.11.5
  - Lombok 1.18.30
  - Caffeine 3.1.8（本地缓存）
  - Redisson 3.23.2（可选分布式缓存）
  - EasyExcel 3.3.2
  - SpringDoc OpenAPI 2.x
  - HikariCP（默认）
  - Maven 3.9+

> 所有模块均严格遵守分层结构：controller → service → repository，利用 DTO-VO-Entity 进行解耦。

## 🗃️ 数据库设计说明

- **数据库名**：`edu_db`
- **数据库引擎**：MySQL 8.x，InnoDB

### 主要数据表（共15张+）
| 表名 | 描述 | 说明 |
|---|---|---|
| user | 用户表 | 包含所有登录账户，字段有用户名、密码、角色等 |
| student | 学生信息 | 与 user 关联，扩展学号、专业、班级 |
| teacher | 教师信息 | 与 user 关联，扩展工号、职称 |
| admin | 管理员 | 系统管理员账户信息 |
| department | 学院信息 | 如“计算机学院”“数学学院” |
| major | 专业信息 | 专业编号、专业所属学院 |
| course | 课程 | 课程编号、学分、授课教师 |
| course_request | 选课申请 | 学生发起申请后待审批的记录 |
| course_selection | 正式选课 | 审批通过后生效的选课信息 |
| grade | 学生成绩 | 学生ID、课程ID、分数、评语 |
| grade_weight | 成绩构成 | 如“期中30%，期末70%” |
| permission | 权限表 | 权限与角色绑定 |
| view_* | 数据库视图 | 如 view_student_score, view_teacher_course |
| stat_* | 存储过程统计结果表 | 如 stat_course_enrollment, stat_gpa_distribution |

### 数据库优化对象
| 类型 | 示例 | 用途 |
|---|---|---|
| 视图 | view_student_score | 汇总学生成绩，提升查询性能 |
| 触发器 | after_grade_update_trigger | 自动计算绩点或课程通过状态 |
| 存储过程 | sp_generate_course_stat() | 批量统计选课人数、通过率 |

## 配置说明

### 核心依赖配置（pom.xml 摘要）
```xml
<spring-boot.version>3.2.0</spring-boot.version>
<jjwt.version>0.11.5</jjwt.version>
<lombok.version>1.18.30</lombok.version>
<redisson.version>3.23.2</redisson.version>
<caffeine.version>3.1.8</caffeine.version>
<easyexcel.version>3.3.2</easyexcel.version>
```

### application.yml 配置示例
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/edu_db
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: change-this-secret-key
  expiration: 3600000 # 1 hour

springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    path: /swagger-ui.html
```

### 缓存配置说明
| 缓存类型         | 用途                     | 技术                      |
|------------------|--------------------------|---------------------------|
| 本地缓存         | 学生成绩、教师课程列表   | Caffeine                  |
| 分布式缓存（可选）| 热点查询/并发保障        | Redisson + Redis          |
| 缓存注解         | 接口级缓存控制           | @Cacheable, @CacheEvict, @CachePut |

### 使用说明

#### 启动步骤
1. 安装并运行 MySQL 数据库，创建数据库 `edu_db`。
2. 配置 `application.yml` 数据库连接信息。
3. 使用 IDE（如 IntelliJ IDEA）打开项目，运行主类 `EduManagementServiceApplication`。
4. 数据表将自动创建（基于 JPA）。
5. 访问 Swagger UI 查看接口文档。

#### 初始账户建议
| 角色   | 账户名   | 密码     |
|--------|----------|----------|
| 管理员 | admin    | admin123 |
| 教师   | teacher1 | 123456   |
| 学生   | student1 | 123456   |

初始数据可通过 `data.sql` 脚本初始化，或通过管理端口录入。

### 权限与安全设计
本系统采用基于 JWT 的身份认证机制，结合 Spring Security 权限控制：

#### 角色权限表
| 角色   | 权限说明                           |
|--------|------------------------------------|
| 管理员 | 管理用户、课程、专业、数据分析等全权限 |
| 教师   | 查看/管理所授课程、录入成绩、导出成绩等 |
| 学生   | 查看课程、提交选课、查询成绩、修改个人信息 |

#### 安全机制说明
- 登录接口返回 JWT Token。
- 后续请求需携带 `Authorization: Bearer <token>` 头。
- 使用 `JwtAuthenticationFilter` 对请求身份进行验证。
- 未授权访问统一返回 401 Unauthorized 错误结构体。

### 接口文档说明
已集成 SpringDoc OpenAPI 自动生成文档：

- Swagger UI 地址：[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI 文档地址（JSON）：[http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### 测试说明
测试代码位于 `src/test/java` 目录下，支持以下类型的测试：

- 单元测试（Service 层逻辑）
- 集成测试（Controller API）
- 安全测试（JWT、权限校验）
- 缓存测试（Caffeine 缓存命中率等）

运行所有测试：
```bash
mvn test
```

### 系统架构说明
- **Controller**    → 接收请求，参数校验，调用服务
- **Service**       → 业务逻辑处理，缓存控制，事务管理
- **Repository**    → 数据库操作接口（JPA），支持自定义 SQL
- **Entity/DTO/VO** → 数据模型与接口解耦
- **Security**      → 身份验证、权限控制、密码加密
- **Cache Layer**   → 使用 Caffeine + Redis 提升访问速度
- **Exception**     → 全局异常捕获与处理（BaseResponse 封装）
### 常见问题
-Q1：运行时报 Lombok 错误怎么办？ A：请确保 IDE 安装了 Lombok 插件，并在 Settings > Build, Execution, Deployment > Compiler 勾选 Annotation Processors。
-Q2：Redis 是必需的吗？A：项目使用 Redisson 支持缓存和分布式锁，若 Redis 不可用，可将 Redisson 相关配置暂时注释掉或配置为本地。
-Q3：数据库初始化 SQL 没有怎么办？A：你可以根据实体类结构手动建表，建议作者提供 init.sql 脚本（可放入 /sql/init.sql）。
