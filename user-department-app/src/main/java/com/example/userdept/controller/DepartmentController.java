package com.example.userdept.controller;

import com.example.userdept.dto.DepartmentResponse;
import com.example.userdept.dto.DepartmentWithUsersResponse;
import com.example.userdept.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // AC1: fetch list of all departments
    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    // AC5: fetch single department by ID with list of active user names
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentWithUsersResponse> getDepartmentById(@PathVariable int id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }
}
