package com.example.learning_platform.dto;

import jakarta.validation.constraints.NotNull;

public record EnrollRequest(@NotNull Long studentId) {}