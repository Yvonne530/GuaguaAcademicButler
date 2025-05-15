package org.example.edumanagementservice.service;


import org.example.edumanagementservice.dto.MajorDTO;

import java.util.List;

public interface MajorService {

    MajorDTO createMajor(MajorDTO majorDTO);

    MajorDTO updateMajor(Long id, MajorDTO majorDTO);

    void deleteMajor(Long id);

    MajorDTO getMajorById(Long id);

    List<MajorDTO> getMajorsByDepartment(Long departmentId);

    List<MajorDTO> getAllMajors();
}
