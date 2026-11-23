package com.example.learning_platform.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTagRequest(@NotBlank String name) {}