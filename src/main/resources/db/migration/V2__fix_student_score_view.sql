-- 修复 v_student_scores 视图：
-- grade.student_id 存储的是学生账号(account)字符串，
-- 视图需通过 students.account 关联，输出学生的数字主键 id，
-- 以匹配 StudentScoreView.studentId(Long) 与按学生ID查询的接口。

CREATE OR REPLACE VIEW v_student_scores AS
SELECT
    g.id                            AS id,
    s.id                            AS student_id,
    s.name                          AS student_name,
    c.course_name                   AS course_name,
    CAST(g.score AS DECIMAL(38, 2)) AS score,
    CASE
        WHEN g.score >= 90 THEN 'A'
        WHEN g.score >= 80 THEN 'B'
        WHEN g.score >= 70 THEN 'C'
        WHEN g.score >= 60 THEN 'D'
        ELSE 'F'
    END                             AS grade
FROM grade g
         LEFT JOIN students s ON s.account = g.student_id
         LEFT JOIN course c ON c.course_code = g.course_code;