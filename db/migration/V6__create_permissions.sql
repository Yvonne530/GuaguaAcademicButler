CREATE TABLE permissions (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(100) NOT NULL UNIQUE,
                             description VARCHAR(255)
);
