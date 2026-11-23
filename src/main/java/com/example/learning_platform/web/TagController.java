package com.example.learning_platform.web;

import com.example.learning_platform.dto.CourseResponse;
import com.example.learning_platform.dto.CreateTagRequest;
import com.example.learning_platform.dto.TagResponse;
import com.example.learning_platform.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping("/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponse create(@Valid @RequestBody CreateTagRequest r) {
        return TagResponse.from(tagService.create(r.name()));
    }

    @GetMapping("/tags")
    public List<TagResponse> all() {
        return tagService.getAll().stream().map(TagResponse::from).toList();
    }

    @GetMapping("/tags/{tagId}/courses")
    public List<CourseResponse> coursesByTag(@PathVariable Long tagId) {
        return tagService.getCoursesByTag(tagId);
    }

    @PostMapping("/courses/{courseId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addToCourse(@PathVariable Long courseId, @PathVariable Long tagId) {
        tagService.addTagToCourse(courseId, tagId);
    }

    @DeleteMapping("/courses/{courseId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFromCourse(@PathVariable Long courseId, @PathVariable Long tagId) {
        tagService.removeTagFromCourse(courseId, tagId);
    }
}