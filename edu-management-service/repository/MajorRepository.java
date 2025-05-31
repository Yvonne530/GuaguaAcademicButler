package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorRepository extends JpaRepository<Major, Long> {

    boolean existsByNameAndDepartmentId(String name, Long departmentId);

    List<Major> findByDepartmentId(Long departmentId);
}
