package org.example.edumanagementservice.dto;


import lombok.Data;
import com.alibaba.excel.annotation.ExcelProperty;

/**
 * Excel导入用教师DTO
 */
@Data
public class TeacherImportDTO {

    @ExcelProperty("教师姓名")
    private String name;

    @ExcelProperty("教师账号")
    private String username;

    @ExcelProperty("职称")
    private String title;

    @ExcelProperty("院系")
    private String department;

    @ExcelProperty("联系电话")
    private String phone;

    @ExcelProperty("邮箱")
    private String email;

    // 可根据需要补充更多字段
}
