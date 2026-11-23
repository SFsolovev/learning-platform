package com.example.learning_platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCourseRequest(
        @NotBlank String title,
        @Size(max = 2000) String description,
        @NotNull Long categoryId,
        @NotNull Long teacherId) {}