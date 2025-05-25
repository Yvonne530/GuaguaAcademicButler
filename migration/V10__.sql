ALTER TABLE course
    DROP FOREIGN KEY course_ibfk_1;

ALTER TABLE course_selection
    DROP FOREIGN KEY course_selection_ibfk_1;

ALTER TABLE major
    DROP FOREIGN KEY major_ibfk_1;

ALTER TABLE score
    DROP FOREIGN KEY score_ibfk_1;

ALTER TABLE score
    DROP FOREIGN KEY score_ibfk_2;

ALTER TABLE course
    ADD semester VARCHAR(255) NULL;

ALTER TABLE course
    ADD CONSTRAINT FK_COURSE_ON_TEACHER FOREIGN KEY (teacher_id) REFERENCES teacher_permission (id);

DROP TABLE department;

DROP TABLE major;

DROP TABLE score;

DROP TABLE student;

DROP TABLE teacher;

ALTER TABLE course
    DROP COLUMN student_count;

ALTER TABLE students
    MODIFY account VARCHAR(255);

ALTER TABLE students
    MODIFY account VARCHAR(255) NULL;

ALTER TABLE users
    MODIFY account VARCHAR(64);

ALTER TABLE grade
    MODIFY course_code VARCHAR(255);

ALTER TABLE grade
    MODIFY course_code VARCHAR(255) NULL;

ALTER TABLE course_requests
    MODIFY course_id BIGINT NULL;

ALTER TABLE course
    MODIFY course_name VARCHAR(255);

ALTER TABLE course
    MODIFY course_name VARCHAR(255) NULL;

ALTER TABLE grade_weight
    MODIFY created_at datetime NULL;

ALTER TABLE admins
    MODIFY dept VARCHAR(255);

ALTER TABLE users
    MODIFY dept VARCHAR(64);

ALTER TABLE course
    MODIFY `description` VARCHAR(255);

ALTER TABLE permissions
    MODIFY name VARCHAR(255);

ALTER TABLE students
    MODIFY name VARCHAR(255);

ALTER TABLE students
    MODIFY name VARCHAR(255) NULL;

ALTER TABLE admins
    MODIFY password VARCHAR(255) NULL;

ALTER TABLE students
    MODIFY password VARCHAR(255);

ALTER TABLE students
    MODIFY password VARCHAR(255) NULL;

ALTER TABLE course_requests
    MODIFY reason VARCHAR(255);

ALTER TABLE course_requests
    DROP COLUMN request_type;

ALTER TABLE course_requests
    DROP COLUMN status;

ALTER TABLE course_requests
    ADD request_type VARCHAR(255) NULL;

ALTER TABLE course_requests
    MODIFY request_type VARCHAR(255) NULL;

ALTER TABLE users
    MODIFY `role` VARCHAR(255);

ALTER TABLE grade
    MODIFY semester VARCHAR(255);

ALTER TABLE grade
    MODIFY semester VARCHAR(255) NULL;

ALTER TABLE course_requests
    ADD status VARCHAR(255) NULL;

ALTER TABLE course_requests
    MODIFY status VARCHAR(255) NULL;

ALTER TABLE course_requests
    MODIFY student_id BIGINT NULL;

ALTER TABLE course_selection
    DROP COLUMN student_id;

ALTER TABLE course_selection
    ADD student_id VARCHAR(255) NULL;

ALTER TABLE course_selection
    MODIFY student_id VARCHAR(255) NULL;

ALTER TABLE grade
    MODIFY student_id VARCHAR(255);

ALTER TABLE grade
    MODIFY student_id VARCHAR(255) NULL;

ALTER TABLE course
    MODIFY teacher_id BIGINT NULL;

ALTER TABLE teacher_permission
    MODIFY teacher_id BIGINT NULL;

ALTER TABLE admins
    MODIFY username VARCHAR(255);

ALTER TABLE admins
    MODIFY username VARCHAR(255) NULL;