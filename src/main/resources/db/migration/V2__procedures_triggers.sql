-- 成绩评级触发器
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

-- 选课人数触发器
DROP TRIGGER IF EXISTS after_course_selection;
//
CREATE TRIGGER after_course_selection
    AFTER INSERT ON course_selection
    FOR EACH ROW
BEGIN
    UPDATE course
    SET student_count = student_count + 1
    WHERE id = NEW.course_id;
END;
//

-- 成绩统计存储过程
DROP PROCEDURE IF EXISTS stat_course_scores;
//
CREATE PROCEDURE stat_course_scores(IN courseId INT)
BEGIN
    SELECT c.course_name,
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
