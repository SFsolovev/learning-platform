package com.example.learning_platform.dto;

import com.example.learning_platform.entity.QuizSubmission;

import java.time.LocalDateTime;

public record QuizResultResponse(Long id, Long quizId, Long studentId, Integer score,
                                 int totalQuestions, LocalDateTime takenAt) {
    public static QuizResultResponse from(QuizSubmission s) {
        return new QuizResultResponse(s.getId(), s.getQuiz().getId(), s.getStudent().getId(),
                s.getScore(), s.getQuiz().getQuestions().size(), s.getTakenAt());
    }
}