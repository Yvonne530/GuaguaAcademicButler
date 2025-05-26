-- ===============================
-- 1. 学院表
-- ===============================
CREATE TABLE IF NOT EXISTS department (
                                          id INT PRIMARY KEY AUTO_INCREMENT,
                                          name VARCHAR(100) NOT NULL
    );

-- ===============================
-- 2. 专业表（依赖 department）
-- ===============================
CREATE TABLE IF NOT EXISTS major (
                                     id INT PRIMARY KEY AUTO_INCREMENT,
                                     name VARCHAR(100) NOT NULL,
    code VARCHAR(10) UNIQUE NOT NULL,
    department_id INT NOT NULL,
    FOREIGN KEY (department_id) REFERENCES department(id)
    );

-- ===============================
-- 3. 教师表
-- ===============================
CREATE TABLE IF NOT EXISTS teacher (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       name VARCHAR(100) NOT NULL
    );

-- ===============================
-- 4. 课程表
-- ===============================
CREATE TABLE IF NOT EXISTS course (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      course_name VARCHAR(100) NOT NULL,
    teacher_id BIGINT NOT NULL,
    student_count INT DEFAULT 0,
    FOREIGN KEY (teacher_id) REFERENCES teacher(id)
    );

-- ===============================
-- 5. 学生表
-- ===============================
CREATE TABLE IF NOT EXISTS student (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       name VARCHAR(100) NOT NULL
    );

-- ===============================
-- V11__create_teacher_permission.sq. 成绩表
-- ===============================
CREATE TABLE IF NOT EXISTS score (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     student_id BIGINT NOT NULL,
                                     course_id BIGINT NOT NULL,
                                     score DECIMAL(5,2) NOT NULL,
    grade CHAR(2),
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
    );

-- ===============================
-- 7. 选课表
-- ===============================
CREATE TABLE IF NOT EXISTS course_selection (
                                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                student_id BIGINT NOT NULL,
                                                course_id BIGINT NOT NULL,
                                                FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
    );

-- ===============================
-- 8. 成绩权重表
-- ===============================
CREATE TABLE IF NOT EXISTS grade_weight (
                                            id BIGINT NOT NULL AUTO_INCREMENT,
                                            course_id BIGINT NOT NULL COMMENT '关联课程ID',
                                            item_name VARCHAR(50) NOT NULL COMMENT '分项名称（如作业、考试）',
    weight DECIMAL(5,2) NOT NULL COMMENT '权重（0.00-1.00）',
    created_by BIGINT NOT NULL COMMENT '设置人（教师ID）',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_course_item (course_id, item_name),
    CONSTRAINT fk_weight_course FOREIGN KEY (course_id) REFERENCES course (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程分数权重配置';

-- ===============================
-- 9. 视图：学生成绩总览
-- ===============================
DROP VIEW IF EXISTS v_student_scores;
CREATE VIEW v_student_scores AS
SELECT
    ROW_NUMBER() OVER () AS id, -- 添加这一行
    s.id AS student_id,
    s.name AS student_name,
    c.id AS course_id,
    c.course_name,
    sc.score,
    sc.grade,
    t.name AS teacher_name
FROM student s
         JOIN score sc ON s.id = sc.student_id
         JOIN course c ON sc.course_id = c.id
         JOIN teacher t ON c.teacher_id = t.id;

-- 10. 触发器：自动设置成绩等级
-- ===============================
DROP TRIGGER IF EXISTS trg_set_grade;
DELIMITER //
CREATE TRIGGER trg_set_grade
    BEFORE INSERT ON score
    FOR EACH ROW
BEGIN
    IF NEW.score >= 90 THEN
        SET NEW.grade = 'A';
    ELSEIF NEW.score >= 80 THEN
        SET NEW.grade = 'B';
    ELSEIF NEW.score >= 70 THEN
        SET NEW.grade = 'C';
    ELSEIF NEW.score >= 60 THEN
        SET NEW.grade = 'D';
    ELSE
        SET NEW.grade = 'F';
END IF;
END;
//
DELIMITER ;

-- ===============================
-- 11. 触发器：选课后更新课程人数
-- ===============================
DROP TRIGGER IF EXISTS after_course_selection;
DELIMITER //
CREATE TRIGGER after_course_selection
    AFTER INSERT ON course_selection
    FOR EACH ROW
BEGIN
    UPDATE course
    SET student_count = student_count + 1
    WHERE id = NEW.course_id;
END;
//
DELIMITER ;

-- ===============================
-- 12. 存储过程：统计课程成绩
-- ===============================
DROP PROCEDURE IF EXISTS stat_course_scores;
DELIMITER //
CREATE PROCEDURE stat_course_scores(IN courseId INT)
BEGIN
SELECT
    c.course_name,
    COUNT(sc.id) AS student_count,
    AVG(sc.score) AS avg_score,
    MAX(sc.score) AS max_score,
    MIN(sc.score) AS min_score
FROM score sc
         JOIN course c ON sc.course_id = c.id
WHERE sc.course_id = courseId
GROUP BY c.course_name;
END;
//
DELIMITER ;