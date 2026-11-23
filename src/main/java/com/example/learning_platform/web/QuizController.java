package com.example.learning_platform.web;

import com.example.learning_platform.dto.*;
import com.example.learning_platform.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/{id}")
    public QuizResponse get(@PathVariable Long id) {
        return quizService.getQuizResponse(id);
    }

    @PostMapping("/{id}/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponse addQuestion(@PathVariable Long id, @Valid @RequestBody CreateQuestionRequest r) {
        return QuestionResponse.from(quizService.addQuestion(id, r.text(), r.type(), r.options()));
    }

    @PostMapping("/{id}/take")
    public QuizResultResponse take(@PathVariable Long id, @Valid @RequestBody TakeQuizRequest r) {
        return quizService.takeQuizResult(id, r.studentId(), r.answers());
    }

    @GetMapping("/{id}/results")
    public List<QuizResultResponse> results(@PathVariable Long id) {
        return quizService.getQuizResults(id);
    }
}