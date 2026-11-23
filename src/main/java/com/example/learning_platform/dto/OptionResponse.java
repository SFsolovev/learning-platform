package com.example.learning_platform.dto;

import com.example.learning_platform.entity.AnswerOption;

public record OptionResponse(Long id, String text) {
    public static OptionResponse from(AnswerOption o) {
        return new OptionResponse(o.getId(), o.getText());   // флаг correct намеренно скрыт
    }
}