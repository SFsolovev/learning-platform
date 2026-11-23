package com.example.learning_platform.web;

import com.example.learning_platform.dto.CreateLessonRequest;
import com.example.learning_platform.dto.LessonResponse;
import com.example.learning_platform.service.CourseService;
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

    @PostMapping("/{moduleId}/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponse addLesson(@PathVariable Long moduleId, @Valid @RequestBody CreateLessonRequest r) {
        return LessonResponse.from(courseService.addLesson(moduleId, r.title(), r.content(), r.videoUrl()));
    }

    @GetMapping("/{moduleId}/lessons")
    public List<LessonResponse> lessons(@PathVariable Long moduleId) {
        return courseService.getLessons(moduleId);
    }
}