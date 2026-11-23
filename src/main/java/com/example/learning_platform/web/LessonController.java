package com.example.learning_platform.web;

import com.example.learning_platform.dto.AssignmentResponse;
import com.example.learning_platform.dto.CreateAssignmentRequest;
import com.example.learning_platform.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final AssignmentService assignmentService;

    @PostMapping("/{lessonId}/assignments")
    @ResponseStatus(HttpStatus.CREATED)
    public AssignmentResponse create(@PathVariable Long lessonId, @Valid @RequestBody CreateAssignmentRequest r) {
        return AssignmentResponse.from(assignmentService.createAssignment(
                lessonId, r.title(), r.description(), r.dueDate(), r.maxScore()));
    }

    @GetMapping("/{lessonId}/assignments")
    public List<AssignmentResponse> list(@PathVariable Long lessonId) {
        return assignmentService.getAssignmentsByLesson(lessonId);
    }
}