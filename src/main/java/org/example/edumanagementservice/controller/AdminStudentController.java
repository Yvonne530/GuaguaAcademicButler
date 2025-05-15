package com.edu.management.controller;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.service.io.ExcelImportService;
import org.example.edumanagementservice.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
@PostMapping("/batch")
public ResponseEntity<?> batchAddStudents(@RequestParam MultipartFile file) {
    List<Student> students = ExcelParser.parseStudents(file);
    return ResponseEntity.ok(studentService.batchSave(students));
}
@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {
    private final ExcelImportService importService;
    private final StudentService studentService;

    @PostMapping("/batch-import")
    public ResponseEntity<?> batchImport(@RequestParam MultipartFile file) {
        List<StudentImportDTO> dtos = importService.importStudents(file);
        List<Student> students = dtos.stream()
                .map(dto -> Student.builder()
                        .name(dto.getName())
                        .account(dto.getAccount())
                        .build())
                .toList();

        studentService.batchSave(students);
        return ResponseEntity.ok(ResponseUtil.success("导入成功"));
    }
}