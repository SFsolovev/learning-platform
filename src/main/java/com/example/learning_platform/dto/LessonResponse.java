package com.example.learning_platform.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import  com.example.learning_platform.entity.*;

public record LessonResponse(Long id, String title, String content, String videoUrl) {
    public static LessonResponse from(Lesson l) {
        return new LessonResponse(l.getId(), l.getTitle(), l.getContent(), l.getVideoUrl());
    }
}