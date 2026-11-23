package com.example.learning_platform.dto;

import com.example.learning_platform.entity.Quiz;

import java.util.List;

public record QuizResponse(Long id, Long moduleId, String title, Integer timeLimit,
                           List<QuestionResponse> questions) {
    public static QuizResponse from(Quiz q) {
        return new QuizResponse(q.getId(), q.getModule().getId(), q.getTitle(), q.getTimeLimit(),
                q.getQuestions().stream().map(QuestionResponse::from).toList());
    }
}