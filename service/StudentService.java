package org.example.edumanagementservice.service;


import org.example.edumanagementservice.model.Student;

import java.util.List;

public interface StudentService {
    void batchSave(List<Student> students);
}
