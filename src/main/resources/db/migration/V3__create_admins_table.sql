CREATE TABLE IF NOT EXISTS admins (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      username VARCHAR(100),
                                      password VARCHAR(255),
                                      dept VARCHAR(100)
);
