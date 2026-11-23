package com.example.learning_platform.web;

import com.example.learning_platform.dto.CreateUserRequest;
import com.example.learning_platform.dto.UserResponse;
import com.example.learning_platform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody CreateUserRequest r) {
        return UserResponse.from(userService.createUser(r.name(), r.email(), r.role()));
    }

    @GetMapping
    public List<UserResponse> all() {
        return userService.getAll().stream().map(UserResponse::from).toList();
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return UserResponse.from(userService.getUser(id));
    }
}