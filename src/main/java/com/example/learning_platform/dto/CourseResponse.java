package com.example.learning_platform.dto;

import com.example.learning_platform.entity.Course;

public record CourseResponse(Long id, String title, String description,
                             String category, String teacher) {
    public static CourseResponse from(Course c) {
        return new CourseResponse(c.getId(), c.getTitle(), c.getDescription(),
                c.getCategory().getName(), c.getTeacher().getName());
    }
}