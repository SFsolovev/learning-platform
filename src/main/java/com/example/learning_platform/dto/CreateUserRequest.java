package com.example.learning_platform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.example.learning_platform.entity.Role;

public record CreateUserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull Role role) {}