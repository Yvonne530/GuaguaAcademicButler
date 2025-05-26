package org.example.edumanagementservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.model.Student;
import org.example.edumanagementservice.repository.StudentRepository;
import org.example.edumanagementservice.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public void batchSave(List<Student> students) {
        studentRepository.saveAll(students);
    }
}
