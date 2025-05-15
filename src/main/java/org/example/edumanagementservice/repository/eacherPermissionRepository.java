package org.example.edumanagementservice.repository;

public interface TeacherPermissionRepository extends JpaRepository<TeacherPermission, Integer> {
    Optional<TeacherPermission> findByTeacherId(Integer teacherId);
}
