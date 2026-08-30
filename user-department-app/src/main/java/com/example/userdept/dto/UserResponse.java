package com.example.userdept.dto;

public record UserResponse(long id, String name, int departmentId, boolean active) {}
