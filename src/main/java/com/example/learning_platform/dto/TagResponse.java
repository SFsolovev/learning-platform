package com.example.learning_platform.dto;

import com.example.learning_platform.entity.Tag;

public record TagResponse(Long id, String name) {
    public static TagResponse from(Tag t) {
        return new TagResponse(t.getId(), t.getName());
    }
}