package org.example.edumanagementservice.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MajorDTO {

    private Long id;

    private String name;

    private String description;

    private Long departmentId;

    private String departmentName;
}
