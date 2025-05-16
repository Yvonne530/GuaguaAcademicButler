package org.example.edumanagementservice.service.io;

import com.alibaba.excel.EasyExcel;
import org.example.edumanagementservice.dto.StudentImportDTO;
import org.example.edumanagementservice.dto.TeacherImportDTO;
import org.example.edumanagementservice.exception.FileProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ExcelImportService {

    public List<StudentImportDTO> importStudents(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            return EasyExcel.read(is)
                    .head(StudentImportDTO.class)
                    .sheet()
                    .doReadSync();
        } catch (IOException e) {
            throw new FileProcessingException("学生Excel文件处理失败", e);
        }
    }

    public List<TeacherImportDTO> importTeachers(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            return EasyExcel.read(is)
                    .head(TeacherImportDTO.class)
                    .sheet()
                    .doReadSync();
        } catch (IOException e) {
            throw new FileProcessingException("教师Excel文件处理失败", e);
        }
    }
}
