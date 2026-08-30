package com.example.userdept.service;

import com.example.userdept.dto.DepartmentResponse;
import com.example.userdept.entity.Department;
import com.example.userdept.entity.User;
import com.example.userdept.exception.ResourceNotFoundException;
import com.example.userdept.repository.DepartmentRepository;
import com.example.userdept.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public DepartmentService(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        List<String> activeUserNames = userRepository.findByDepartmentIdAndActiveTrue(id)
                .stream()
                .map(User::getName)
                .toList();

        return new DepartmentResponse(department.getId(), department.getName(), activeUserNames);
    }
}
