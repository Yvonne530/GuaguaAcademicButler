package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
