package org.example.edumanagementservice.service.impl;


import org.example.edumanagementservice.dto.MajorDTO;
import org.example.edumanagementservice.exception.CustomException;
import org.example.edumanagementservice.model.Department;
import org.example.edumanagementservice.model.Major;
import org.example.edumanagementservice.repository.DepartmentRepository;
import org.example.edumanagementservice.repository.MajorRepository;
import org.example.edumanagementservice.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public MajorDTO createMajor(MajorDTO dto) {
        if (majorRepository.existsByNameAndDepartmentId(dto.getName(), dto.getDepartmentId())) {
            throw new CustomException("该院系下已存在同名专业");
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new CustomException("所属院系不存在"));

        Major major = Major.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .department(department)
                .build();

        return convertToDTO(majorRepository.save(major));
    }

    @Override
    public MajorDTO updateMajor(Long id, MajorDTO dto) {
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new CustomException("未找到对应专业"));

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new CustomException("所属院系不存在"));

        major.setName(dto.getName());
        major.setDescription(dto.getDescription());
        major.setDepartment(department);

        return convertToDTO(majorRepository.save(major));
    }

    @Override
    public void deleteMajor(Long id) {
        if (!majorRepository.existsById(id)) {
            throw new CustomException("专业不存在");
        }
        majorRepository.deleteById(id);
    }

    @Override
    public MajorDTO getMajorById(Long id) {
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new CustomException("未找到对应专业"));
        return convertToDTO(major);
    }

    @Override
    public List<MajorDTO> getMajorsByDepartment(Long departmentId) {
        return majorRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable("majors")
    public List<MajorDTO> getAllMajors() {
        return majorRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MajorDTO convertToDTO(Major major) {
        return MajorDTO.builder()
                .id(major.getId())
                .name(major.getName())
                .description(major.getDescription())
                .departmentId(major.getDepartment().getId())
                .departmentName(major.getDepartment().getName())
                .build();
    }
}
