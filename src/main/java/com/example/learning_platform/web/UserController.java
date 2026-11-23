package com.example.learning_platform.web;

import com.example.learning_platform.dto.CreateUserRequest;
import com.example.learning_platform.dto.EnrollmentResponse;
import com.example.learning_platform.dto.SubmissionResponse;
import com.example.learning_platform.dto.UserResponse;
import com.example.learning_platform.service.AssignmentService;
import com.example.learning_platform.service.EnrollmentService;
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
    private final AssignmentService assignmentService;
    private final EnrollmentService enrollmentService;

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

    @GetMapping("/{id}/submissions")
    public List<SubmissionResponse> submissions(@PathVariable Long id) {
        return assignmentService.getStudentSubmissions(id);
    }

    @GetMapping("/{id}/enrollments")
    public List<EnrollmentResponse> enrollments(@PathVariable Long id) {
        return enrollmentService.getStudentEnrollments(id);
    }
}