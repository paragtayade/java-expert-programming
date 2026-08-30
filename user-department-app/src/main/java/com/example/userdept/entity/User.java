package com.example.userdept.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(nullable = false)
    private boolean active = true;

    public User() {}

    public User(String name, Long departmentId) {
        this.name = name;
        this.departmentId = departmentId;
        this.active = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
