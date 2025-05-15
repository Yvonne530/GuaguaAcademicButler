CREATE TABLE major (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(100) NOT NULL,
                       code VARCHAR(10) UNIQUE NOT NULL,
                       department_id INT NOT NULL,
                       FOREIGN KEY (department_id) REFERENCES department(id)
);

CREATE VIEW v_student_scores AS
SELECT
    s.id AS student_id,
    s.name AS student_name,
    c.id AS course_id,
    c.course_name,
    sc.score,
    sc.grade,
    t.name AS teacher_name
FROM
    student s
        JOIN score sc ON s.id = sc.student_id
        JOIN course c ON sc.course_id = c.id
        JOIN teacher t ON c.teacher_id = t.id;
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
-- 选课人数自动更新
DELIMITER //
CREATE TRIGGER after_course_selection
    AFTER INSERT ON course_selection
    FOR EACH ROW
BEGIN
    UPDATE course
    SET student_count = student_count + 1
    WHERE id = NEW.course_id;
END//
//

DELIMITER ;
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
CREATE TABLE `grade_weight` (
                                `id` BIGINT NOT NULL AUTO_INCREMENT,
                                `course_id` BIGINT NOT NULL COMMENT '关联课程ID',
                                `item_name` VARCHAR(50) NOT NULL COMMENT '分项名称（如作业、考试）',
                                `weight` DECIMAL(5,2) NOT NULL COMMENT '权重（0.00-1.00）',
                                `created_by` BIGINT NOT NULL COMMENT '设置人（教师ID）',
                                `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `uk_course_item` (`course_id`, `item_name`),
                                CONSTRAINT `fk_weight_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程分数权重配置';

DELIMITER ;
