package org.example.edumanagementservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.edumanagementservice.dto.DepartmentDTO;
import org.example.edumanagementservice.exception.CustomException;
import org.example.edumanagementservice.model.Department;
import org.example.edumanagementservice.repository.DepartmentRepository;
import org.example.edumanagementservice.service.DepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        if (departmentRepository.existsByName(dto.getName())) {
            throw new CustomException("院系名称已存在");
        }
        Department department = Department.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        department = departmentRepository.save(department);
        return convertToDTO(department);
    }

    @Override
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new CustomException("未找到对应院系"));
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        return convertToDTO(departmentRepository.save(department));
    }

    @Override
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new CustomException("院系不存在");
        }
        departmentRepository.deleteById(id);
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new CustomException("未找到对应院系"));
        return convertToDTO(department);
    }

    @Override
    @Cacheable("departments")
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        BeanUtils.copyProperties(department, dto);
        return dto;
    }
}
