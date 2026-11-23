package com.example.learning_platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateQuizRequest(@NotBlank String title, @NotNull @Positive Integer timeLimit) {}
