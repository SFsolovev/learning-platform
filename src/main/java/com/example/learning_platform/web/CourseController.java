package com.example.learning_platform.web;

import com.example.learning_platform.dto.*;
import com.example.learning_platform.service.CourseService;
import com.example.learning_platform.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse create(@Valid @RequestBody CreateCourseRequest r) {
        return CourseResponse.from(courseService.createCourse(
                r.title(), r.description(), r.categoryId(), r.teacherId()));
    }

    @GetMapping
    public List<CourseResponse> all() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public CourseResponse get(@PathVariable Long id) {
        return courseService.getCourseResponse(id);
    }

    @PutMapping("/{id}")
    public CourseResponse update(@PathVariable Long id, @Valid @RequestBody UpdateCourseRequest r) {
        courseService.updateDescription(id, r.description());
        return courseService.getCourseResponse(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @PostMapping("/{id}/enrollments")
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentResponse enroll(@PathVariable Long id, @Valid @RequestBody EnrollRequest r) {
        return EnrollmentResponse.from(enrollmentService.enrollStudent(id, r.studentId()));
    }

    @GetMapping("/{id}/enrollments")
    public List<EnrollmentResponse> enrollments(@PathVariable Long id) {
        return enrollmentService.getByCourse(id).stream().map(EnrollmentResponse::from).toList();
    }

    @DeleteMapping("/{id}/enrollments/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unenroll(@PathVariable Long id, @PathVariable Long studentId) {
        enrollmentService.unenroll(id, studentId);
    }

    @GetMapping("/{id}/structure")
    public CourseStructureResponse structure(@PathVariable Long id) {
        return courseService.getCourseStructure(id);
    }

    @PostMapping("/{id}/modules")
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleResponse addModule(@PathVariable Long id, @Valid @RequestBody CreateModuleRequest r) {
        return ModuleResponse.from(courseService.addModule(id, r.title(), r.orderIndex(), r.description()));
    }

    @PutMapping("/{id}/teacher")
    public CourseResponse changeTeacher(@PathVariable Long id, @Valid @RequestBody ChangeTeacherRequest r) {
        courseService.changeTeacher(id, r.teacherId());
        return courseService.getCourseResponse(id);
    }
}