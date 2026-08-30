package com.example.userdept.dto;

import java.util.List;

public class DepartmentResponse {

    private Long id;
    private String name;
    private List<String> activeUserNames;

    public DepartmentResponse(Long id, String name, List<String> activeUserNames) {
        this.id = id;
        this.name = name;
        this.activeUserNames = activeUserNames;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<String> getActiveUserNames() { return activeUserNames; }
}
