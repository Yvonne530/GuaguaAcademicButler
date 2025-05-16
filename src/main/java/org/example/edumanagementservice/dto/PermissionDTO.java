package org.example.edumanagementservice.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDTO {
    private Long id;
    private String name; // teacherId 字符串形式
    private String description; // canPublishCourse 字符串形式
}
