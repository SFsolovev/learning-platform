package com.example.learning_platform.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import  com.example.learning_platform.entity.*;

public record CreateLessonRequest(
        @NotBlank String title,
        String content,
        String videoUrl) {}
