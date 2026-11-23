package com.example.learning_platform.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import  com.example.learning_platform.entity.*;

public record ModuleResponse(Long id, String title, Integer orderIndex,
                             String description, List<LessonResponse> lessons) {
    public static ModuleResponse from(CourseModule m) {
        return new ModuleResponse(m.getId(), m.getTitle(), m.getOrderIndex(), m.getDescription(),
                m.getLessons().stream().map(LessonResponse::from).toList());
    }
}