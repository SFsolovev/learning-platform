package com.example.learning_platform.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import  com.example.learning_platform.entity.*;

public record SubmissionResponse(Long id, Long assignmentId, Long studentId,
                                 LocalDateTime submittedAt, String content,
                                 Integer score, String feedback) {
    public static SubmissionResponse from(Submission s) {
        return new SubmissionResponse(s.getId(), s.getAssignment().getId(), s.getStudent().getId(),
                s.getSubmittedAt(), s.getContent(), s.getScore(), s.getFeedback());
    }
}