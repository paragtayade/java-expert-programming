package com.example.userdept.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateUserRequest(
        @NotBlank String name,
        @Positive int departmentId
) {}
