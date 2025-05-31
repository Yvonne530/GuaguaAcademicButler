CREATE TABLE departments (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(64) NOT NULL UNIQUE,
                             description VARCHAR(256)
);