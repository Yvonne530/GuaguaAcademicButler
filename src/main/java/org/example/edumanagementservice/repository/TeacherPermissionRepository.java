package org.example.edumanagementservice.repository;

import org.example.edumanagementservice.model.TeacherPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherPermissionRepository extends JpaRepository<TeacherPermission, Integer> {
    Optional<TeacherPermission> findByTeacherId(Integer teacherId);
}