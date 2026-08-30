package com.example.userdept.config;

import com.example.userdept.entity.Department;
import com.example.userdept.repository.DepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seedDepartments(DepartmentRepository departmentRepository) {
        return args -> {
            if (departmentRepository.count() == 0) {
                departmentRepository.saveAll(List.of(
                        new Department(100L, "Administration"),
                        new Department(200L, "Engineering"),
                        new Department(300L, "Finance"),
                        new Department(400L, "Sales")
                ));
            }
        };
    }
}
