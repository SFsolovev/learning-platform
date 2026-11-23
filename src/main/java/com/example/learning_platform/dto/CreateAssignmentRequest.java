package com.example.learning_platform.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import  com.example.learning_platform.entity.*;

public record CreateAssignmentRequest(
        @NotBlank String title,
        String description,
        LocalDate dueDate,
        @NotNull @Positive Integer maxScore) {}