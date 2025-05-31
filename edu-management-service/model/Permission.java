package org.example.edumanagementservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 权限名称，例如 "VIEW_COURSE", "EDIT_STUDENT"
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 权限描述，例如 "查看课程信息"
     */
    private String description;
}
