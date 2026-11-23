package com.example.learning_platform.web;

import com.example.learning_platform.dto.SubmissionResponse;
import com.example.learning_platform.dto.SubmitRequest;
import com.example.learning_platform.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;

    @PostMapping("/{id}/submit")
    @ResponseStatus(HttpStatus.CREATED)
    public SubmissionResponse submit(@PathVariable Long id, @Valid @RequestBody SubmitRequest r) {
        return SubmissionResponse.from(assignmentService.submitAssignment(id, r.studentId(), r.content()));
    }

    @GetMapping("/{id}/submissions")
    public List<SubmissionResponse> submissions(@PathVariable Long id) {
        return assignmentService.getSubmissionResponses(id);
    }
}