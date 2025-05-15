package org.example.edumanagementservice.service.io;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class ExcelImportService {

    public List<StudentImportDTO> importStudents(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            ExcelReader reader = EasyExcel.read(is)
                    .head(StudentImportDTO.class)
                    .sheet()
                    .doReadSync();
            return reader;
        } catch (IOException e) {
            throw new FileProcessingException("Excel文件处理失败");
        }
    }

    public List<TeacherImportDTO> importTeachers(MultipartFile file) {
        // 类似实现...
    }

    public List<StudentImportDTO> importStudents(MultipartFile file) {
    }
}