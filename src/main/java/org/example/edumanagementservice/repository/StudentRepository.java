package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByAccount(String account);
}
