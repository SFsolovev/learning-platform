package com.example.learning_platform.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import  com.example.learning_platform.entity.*;

public record AssignmentResponse(Long id, Long lessonId, String title, String description,
                                 LocalDate dueDate, Integer maxScore) {
    public static AssignmentResponse from(Assignment a) {
        return new AssignmentResponse(a.getId(), a.getLesson().getId(), a.getTitle(),
                a.getDescription(), a.getDueDate(), a.getMaxScore());
    }
}