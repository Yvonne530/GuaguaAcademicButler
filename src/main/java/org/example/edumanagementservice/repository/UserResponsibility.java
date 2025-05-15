package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.enums.RoleType;
import org.example.edumanagementservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccount(String account);
    List<User> findByRole(RoleType role);
    void deleteByAccount(String account);
}
