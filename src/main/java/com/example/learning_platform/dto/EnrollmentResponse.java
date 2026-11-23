package com.example.learning_platform.dto;

import com.example.learning_platform.entity.Enrollment;
import com.example.learning_platform.entity.EnrollmentStatus;

import java.time.LocalDate;

public record EnrollmentResponse(Long id, Long studentId, Long courseId,
                                 LocalDate enrollDate, EnrollmentStatus status) {
    public static EnrollmentResponse from(Enrollment e) {
        return new EnrollmentResponse(e.getId(), e.getUser().getId(), e.getCourse().getId(),
                e.getEnrollDate(), e.getStatus());
    }
}