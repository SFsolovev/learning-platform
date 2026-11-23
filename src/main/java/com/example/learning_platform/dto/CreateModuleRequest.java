package com.example.learning_platform.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import  com.example.learning_platform.entity.*;

public record CreateModuleRequest(
        @NotBlank String title,
        @NotNull @Positive Integer orderIndex,
        @Size(max = 1000) String description) {}