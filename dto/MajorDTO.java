package org.example.edumanagementservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MajorDTO {

    private Long id;
    @NotBlank(message = "专业名称不能为空")
    private String name;

    private String description;
    @NotNull(message = "所属院系ID不能为空")
    private Long departmentId;

    private String departmentName;
}
