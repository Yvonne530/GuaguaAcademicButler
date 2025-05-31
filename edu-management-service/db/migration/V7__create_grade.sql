CREATE TABLE grade (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       student_id VARCHAR(50) NOT NULL,
                       course_code VARCHAR(50) NOT NULL,
                       semester VARCHAR(20) NOT NULL,
                       score DOUBLE
);
