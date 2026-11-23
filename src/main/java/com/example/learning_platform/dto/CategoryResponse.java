package com.example.learning_platform.dto;

import com.example.learning_platform.entity.Category;

public record CategoryResponse(Long id, String name) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(c.getId(), c.getName());
    }
}