package com.example.learning_platform.dto;

import com.example.learning_platform.entity.CourseReview;

import java.time.LocalDateTime;

public record ReviewResponse(Long id, Long courseId, Long studentId, Integer rating,
                             String comment, LocalDateTime createdAt) {
    public static ReviewResponse from(CourseReview r) {
        return new ReviewResponse(r.getId(), r.getCourse().getId(), r.getStudent().getId(),
                r.getRating(), r.getComment(), r.getCreatedAt());
    }
}
