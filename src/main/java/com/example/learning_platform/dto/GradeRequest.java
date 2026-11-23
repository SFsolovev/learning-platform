package com.example.learning_platform.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import  com.example.learning_platform.entity.*;

public record GradeRequest(@NotNull @Min(0) Integer score, String feedback) {}