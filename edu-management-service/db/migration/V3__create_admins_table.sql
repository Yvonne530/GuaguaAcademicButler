CREATE TABLE IF NOT EXISTS admins (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    dept VARCHAR(100)
    );
