package com.example.learning_platform.web;

import com.example.learning_platform.dto.GradeRequest;
import com.example.learning_platform.dto.SubmissionResponse;
import com.example.learning_platform.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final AssignmentService assignmentService;

    @PutMapping("/{id}/grade")
    public SubmissionResponse grade(@PathVariable Long id, @Valid @RequestBody GradeRequest r) {
        return assignmentService.grade(id, r.score(), r.feedback());
    }
}