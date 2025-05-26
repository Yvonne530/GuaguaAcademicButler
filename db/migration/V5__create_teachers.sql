CREATE TABLE teachers (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          username VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          full_name VARCHAR(100),
                          email VARCHAR(100),
                          phone VARCHAR(20)
);
