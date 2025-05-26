CREATE TABLE majors (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(64) NOT NULL,
                        description VARCHAR(256),
                        department_id BIGINT NOT NULL,
                        CONSTRAINT fk_major_department FOREIGN KEY (department_id) REFERENCES departments(id)
);