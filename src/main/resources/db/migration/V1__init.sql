-- =====================================================================
-- 呱呱学术管家 edu_db 初始化脚本（与 JPA 实体严格对齐）
-- 由 Flyway 在应用首次启动时自动执行
-- =====================================================================

-- ---------------------------------------------------------------------
-- 1. 用户 / 账户类表
-- ---------------------------------------------------------------------

CREATE TABLE admins (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    dept     VARCHAR(100) NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE teachers (
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    username  VARCHAR(50)  NOT NULL,
    password  VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NULL,
    email     VARCHAR(100) NULL,
    phone     VARCHAR(20)  NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_teachers_username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE students (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NULL,
    account  VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_students_account (account)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE users (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    account  VARCHAR(64)  NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL,
    dept     VARCHAR(64)  NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_account (account)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ---------------------------------------------------------------------
-- 2. 组织架构：学院 / 专业 / 权限
-- ---------------------------------------------------------------------

CREATE TABLE departments (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(64)  NOT NULL,
    description VARCHAR(256) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_departments_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE majors (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    name          VARCHAR(64)  NOT NULL,
    description   VARCHAR(256) NULL,
    department_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    KEY fk_majors_department (department_id),
    CONSTRAINT fk_majors_department FOREIGN KEY (department_id) REFERENCES departments (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE permissions (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_permissions_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE teacher_permission (
    id                 BIGINT      NOT NULL AUTO_INCREMENT,
    teacher_id         BIGINT      NULL,
    can_publish_course TINYINT(1)  DEFAULT 0,
    PRIMARY KEY (id),
    KEY idx_teacher_permission_teacher (teacher_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ---------------------------------------------------------------------
-- 3. 课程 / 选课 / 成绩
-- ---------------------------------------------------------------------

CREATE TABLE course (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    course_name VARCHAR(255) NULL,
    teacher_id  BIGINT       NULL COMMENT '指向 teacher_permission.id',
    course_code VARCHAR(255) NULL,
    description VARCHAR(255) NULL,
    status      VARCHAR(255) NULL,
    semester    VARCHAR(255) NULL,
    PRIMARY KEY (id),
    KEY fk_course_teacher_permission (teacher_id),
    CONSTRAINT fk_course_teacher_permission FOREIGN KEY (teacher_id) REFERENCES teacher_permission (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE course_requests (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    course_id    BIGINT       NULL,
    student_id   BIGINT       NULL,
    request_type VARCHAR(255) NULL COMMENT 'OPEN / CLOSE',
    status       VARCHAR(255) NULL COMMENT 'PENDING / APPROVED / REJECTED',
    reason       VARCHAR(255) NULL,
    created_at   DATETIME     NULL,
    reviewed_at  DATETIME     NULL,
    reviewer     VARCHAR(255) NULL,
    PRIMARY KEY (id),
    KEY idx_course_requests_course (course_id),
    KEY idx_course_requests_student (student_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE course_selection (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    student_id BIGINT       NULL,
    course_id  BIGINT       NOT NULL,
    semester   VARCHAR(255) NULL,
    PRIMARY KEY (id),
    KEY fk_course_selection_course (course_id),
    CONSTRAINT fk_course_selection_course FOREIGN KEY (course_id) REFERENCES course (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE grade (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    student_id VARCHAR(50) NULL COMMENT '学生账号(account)',
    course_code VARCHAR(50) NULL COMMENT '课程编号(course_code)',
    semester   VARCHAR(20) NULL,
    score      DOUBLE      NULL,
    PRIMARY KEY (id),
    KEY idx_grade_student (student_id),
    KEY idx_grade_course (course_code)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

CREATE TABLE grade_weight (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    course_id  BIGINT       NOT NULL COMMENT '关联课程ID',
    item_name  VARCHAR(50)  NOT NULL COMMENT '分项名称（如作业、考试）',
    weight     DECIMAL(5,2) NOT NULL COMMENT '权重（0.00-1.00）',
    created_by BIGINT       NOT NULL COMMENT '设置人（教师ID）',
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_course_item (course_id, item_name),
    CONSTRAINT fk_weight_course FOREIGN KEY (course_id) REFERENCES course (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT ='课程分数权重配置';

-- ---------------------------------------------------------------------
-- 4. 视图：学生成绩汇总（供 StudentScoreView 实体使用）
-- ---------------------------------------------------------------------

CREATE VIEW v_student_scores AS
SELECT
    g.id                                        AS id,
    CAST(g.student_id AS SIGNED)                AS student_id,
    COALESCE(s.name, g.student_id)              AS student_name,
    c.course_name                               AS course_name,
    CAST(g.score AS DECIMAL(38, 2))             AS score,
    CASE
        WHEN g.score >= 90 THEN 'A'
        WHEN g.score >= 80 THEN 'B'
        WHEN g.score >= 70 THEN 'C'
        WHEN g.score >= 60 THEN 'D'
        ELSE 'F'
    END                                         AS grade
FROM grade g
         LEFT JOIN course c ON c.course_code = g.course_code
         LEFT JOIN students s ON s.account = g.student_id;

-- ---------------------------------------------------------------------
-- 5. 存储过程：课程成绩统计（供 ScoreStatRepository 使用）
-- ---------------------------------------------------------------------

DELIMITER $$
CREATE PROCEDURE stat_course_scores(IN courseId INT)
BEGIN
    SELECT
        c.course_name                    AS courseName,
        COUNT(g.id)                      AS studentCount,
        IFNULL(ROUND(AVG(g.score), 2), 0) AS avgScore,
        IFNULL(MAX(g.score), 0)          AS maxScore,
        IFNULL(MIN(g.score), 0)          AS minScore
    FROM course c
             LEFT JOIN grade g ON g.course_code = c.course_code
    WHERE c.id = courseId
    GROUP BY c.id, c.course_name;
END$$
DELIMITER ;

-- =====================================================================
-- 6. 种子数据（密码均为 BCrypt 加密）
--    admin   / admin123
--    teacher1 / 123456
--    student1 / 123456
-- =====================================================================

INSERT INTO departments (id, name, description) VALUES
    (1, '计算机学院', '信息技术相关专业'),
    (2, '数学学院', '数学与应用数学相关专业');

INSERT INTO majors (id, name, description, department_id) VALUES
    (1, '计算机科学与技术', '本科专业', 1),
    (2, '软件工程', '本科专业', 1);

INSERT INTO permissions (id, name, description) VALUES
    (1, 'VIEW_COURSE', '查看课程信息'),
    (2, 'EDIT_STUDENT', '管理学生信息'),
    (3, 'PUBLISH_COURSE', '发布课程'),
    (4, 'INPUT_GRADE', '录入成绩');

INSERT INTO admins (id, username, password, dept) VALUES
    (1, 'admin', '$2a$10$S3.zy8sc5w0hhHIziYWdPO/SDAG6A2BJgrrTFYpmEKDmi4xwM/myq', '信息中心');

INSERT INTO teachers (id, username, password, full_name, email, phone) VALUES
    (1, 'teacher1', '$2a$10$y.fcNY/LoXb3EAhzc5yqxuGw5AWoeT82ugOefa.fCcOHzncY8FCsa', '张老师', 'teacher1@edu.cn', '13800000001'),
    (2, 'teacher2', '$2a$10$y.fcNY/LoXb3EAhzc5yqxuGw5AWoeT82ugOefa.fCcOHzncY8FCsa', '李老师', 'teacher2@edu.cn', '13800000002');

INSERT INTO teacher_permission (id, teacher_id, can_publish_course) VALUES
    (1, 1, 1),
    (2, 2, 1);

INSERT INTO students (id, name, account, password) VALUES
    (1, '王小瓜', 'student1', '$2a$10$y.fcNY/LoXb3EAhzc5yqxuGw5AWoeT82ugOefa.fCcOHzncY8FCsa'),
    (2, '刘小呱', 'student2', '$2a$10$y.fcNY/LoXb3EAhzc5yqxuGw5AWoeT82ugOefa.fCcOHzncY8FCsa');

INSERT INTO users (id, account, password, role, dept) VALUES
    (1, 'admin',    '$2a$10$S3.zy8sc5w0hhHIziYWdPO/SDAG6A2BJgrrTFYpmEKDmi4xwM/myq', 'ADMIN',    '信息中心'),
    (2, 'teacher1', '$2a$10$y.fcNY/LoXb3EAhzc5yqxuGw5AWoeT82ugOefa.fCcOHzncY8FCsa', 'TEACHER',  '计算机学院'),
    (3, 'student1', '$2a$10$y.fcNY/LoXb3EAhzc5yqxuGw5AWoeT82ugOefa.fCcOHzncY8FCsa', 'STUDENT',  '计算机学院');

INSERT INTO course (id, course_name, teacher_id, course_code, description, status, semester) VALUES
    (1, 'Java程序设计', 1, 'CS101', 'Java 语言基础与面向对象编程', 'OPEN', '2025春季'),
    (2, '数据结构', 2, 'CS201', '线性表、树、图等数据结构', 'OPEN', '2025春季');

INSERT INTO grade_weight (id, course_id, item_name, weight, created_by) VALUES
    (1, 1, '期中考试', 0.30, 1),
    (2, 1, '期末考试', 0.70, 1);

INSERT INTO grade (id, student_id, course_code, semester, score) VALUES
    (1, 'student1', 'CS101', '2025春季', 92.0),
    (2, 'student2', 'CS101', '2025春季', 78.5),
    (3, 'student1', 'CS201', '2025春季', 85.0);

INSERT INTO course_selection (id, student_id, course_id, semester) VALUES
    (1, 1, 1, '2025春季'),
    (2, 2, 1, '2025春季');