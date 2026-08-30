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

    // Return all departments in the system.
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Fetch one department and include the names of all currently active users in it.
    public DepartmentResponse getDepartmentById(Long id) {
        // Make sure the department actually exists before building the response.
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        // Get only active users assigned to this department and list their names.
        List<String> activeUserNames = userRepository.findByDepartmentIdAndActiveTrue(id)
                .stream()
                .map(User::getName)
                .toList();

        // Return the department details plus the names of active users.
        return new DepartmentResponse(department.getId(), department.getName(), activeUserNames);
    }
}
