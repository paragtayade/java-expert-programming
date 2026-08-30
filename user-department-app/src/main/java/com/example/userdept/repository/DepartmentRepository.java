package com.example.userdept.repository;

import com.example.userdept.model.Department;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentRepository {

    private final List<Department> departments = new ArrayList<>();

    public DepartmentRepository() {
        departments.add(new Department(100, "Administration"));
        departments.add(new Department(200, "Engineering"));
        departments.add(new Department(300, "Finance"));
        departments.add(new Department(400, "Sales"));
    }

    public List<Department> findAll() {
        return List.copyOf(departments);
    }

    public Optional<Department> findById(int id) {
        return departments.stream()
                .filter(d -> d.getId() == id)
                .findFirst();
    }

    public boolean existsById(int id) {
        return departments.stream().anyMatch(d -> d.getId() == id);
    }
}
