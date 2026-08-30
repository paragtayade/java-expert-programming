package com.example.userdept.dto;

import java.util.List;

public record DepartmentWithUsersResponse(int id, String name, List<String> activeUserNames) {}
