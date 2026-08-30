package com.example.userdept.service;

import com.example.userdept.dto.DepartmentResponse;
import com.example.userdept.dto.DepartmentWithUsersResponse;
import com.example.userdept.model.Department;
import com.example.userdept.model.User;
import com.example.userdept.repository.DepartmentRepository;
import com.example.userdept.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public DepartmentService(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(d -> new DepartmentResponse(d.getId(), d.getName()))
                .toList();
    }

    public DepartmentWithUsersResponse getDepartmentById(int id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Department not found: " + id));

        List<String> activeUserNames = userRepository.findAll().stream()
                .filter(u -> u.getDepartmentId() == id && u.isActive())
                .map(User::getName)
                .toList();

        return new DepartmentWithUsersResponse(department.getId(), department.getName(), activeUserNames);
    }
}
