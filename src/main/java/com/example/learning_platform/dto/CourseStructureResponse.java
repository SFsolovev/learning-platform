package com.example.learning_platform.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import  com.example.learning_platform.entity.*;

public record CourseStructureResponse(Long id, String title, String description,
                                      String category, String teacher,
                                      List<ModuleResponse> modules) {
    public static CourseStructureResponse from(Course c, List<ModuleResponse> modules) {
        return new CourseStructureResponse(c.getId(), c.getTitle(), c.getDescription(),
                c.getCategory().getName(), c.getTeacher().getName(), modules);
    }
}