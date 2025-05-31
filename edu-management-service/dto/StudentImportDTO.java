package org.example.edumanagementservice.dto;

import lombok.Data;
import com.alibaba.excel.annotation.ExcelProperty;

/**
 * Excel导入用学生DTO
 */
@Data
public class StudentImportDTO {

    @ExcelProperty("学生姓名")
    private String name;

    @ExcelProperty("学生账号")
    private String username;

    @ExcelProperty("性别")
    private String gender;

    @ExcelProperty("专业")
    private String major;

    @ExcelProperty("年级")
    private String grade;

    @ExcelProperty("联系方式")
    private String phone;

    @ExcelProperty("邮箱")
    private String email;

    // 你可以根据Excel实际字段继续添加
}
