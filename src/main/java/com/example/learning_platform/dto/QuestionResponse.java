package com.example.learning_platform.dto;

import com.example.learning_platform.entity.Question;
import com.example.learning_platform.entity.QuestionType;

import java.util.List;

public record QuestionResponse(Long id, String text, QuestionType type, List<OptionResponse> options) {
    public static QuestionResponse from(Question q) {
        return new QuestionResponse(q.getId(), q.getText(), q.getType(),
                q.getOptions().stream().map(OptionResponse::from).toList());
    }
}