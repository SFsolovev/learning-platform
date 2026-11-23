package com.example.learning_platform.web;

import com.example.learning_platform.dto.CreateLessonRequest;
import com.example.learning_platform.dto.CreateQuizRequest;
import com.example.learning_platform.dto.LessonResponse;
import com.example.learning_platform.dto.QuizResponse;
import com.example.learning_platform.service.CourseService;
import com.example.learning_platform.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {
    private final CourseService courseService;
    private final QuizService quizService;

    @PostMapping("/{moduleId}/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponse addLesson(@PathVariable Long moduleId, @Valid @RequestBody CreateLessonRequest r) {
        return LessonResponse.from(courseService.addLesson(moduleId, r.title(), r.content(), r.videoUrl()));
    }

    @GetMapping("/{moduleId}/lessons")
    public List<LessonResponse> lessons(@PathVariable Long moduleId) {
        return courseService.getLessons(moduleId);
    }

    @PostMapping("/{moduleId}/quiz")
    @ResponseStatus(HttpStatus.CREATED)
    public QuizResponse createQuiz(@PathVariable Long moduleId, @Valid @RequestBody CreateQuizRequest r) {
        quizService.createQuiz(moduleId, r.title(), r.timeLimit());
        return quizService.getQuizResponseByModule(moduleId);
    }

    @GetMapping("/{moduleId}/quiz")
    public QuizResponse quiz(@PathVariable Long moduleId) {
        return quizService.getQuizResponseByModule(moduleId);
    }
}