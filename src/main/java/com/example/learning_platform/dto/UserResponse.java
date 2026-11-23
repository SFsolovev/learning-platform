package com.example.learning_platform.dto;
import com.example.learning_platform.entity.Role;
import com.example.learning_platform.entity.User;

public record UserResponse(Long id, String name, String email, Role role) {
    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole());
    }
}