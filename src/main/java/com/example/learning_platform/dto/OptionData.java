package com.example.learning_platform.dto;

import jakarta.validation.constraints.NotBlank;

public record OptionData(@NotBlank String text, boolean correct) {}