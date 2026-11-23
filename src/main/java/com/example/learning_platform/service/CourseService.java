package com.example.learning_platform.service;

import com.example.learning_platform.dto.CourseResponse;
import com.example.learning_platform.entity.*;
import com.example.learning_platform.entity.CourseRepository;
import com.example.learning_platform.entity.UserRepository;
import com.example.learning_platform.exception.*;
import com.example.learning_platform.repository.CategoryRepository;
import com.example.learning_platform.repository.LessonRepository;
import com.example.learning_platform.repository.ModuleRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    public Course createCourse(String title, String description, Long categoryId, Long teacherId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена: " + categoryId));
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + teacherId));
        if (teacher.getRole() != Role.TEACHER) {
            throw new BusinessException("Создавать курсы может только преподаватель");
        }
        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setCategory(category);
        course.setTeacher(teacher);
        log.info("Создан курс '{}'", title);
        return courseRepository.save(course);
    }

    @Transactional
    public CourseModule addModule(Long courseId, String title, int orderIndex) {
        Course course = getCourse(courseId);
        CourseModule module = new CourseModule();
        module.setTitle(title);
        module.setOrderIndex(orderIndex);
        course.addModule(module);          // синхронизирует обе стороны связи
        return moduleRepository.save(module);
    }

    @Transactional
    public Lesson addLesson(Long moduleId, String title, String content) {
        CourseModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new NotFoundException("Модуль не найден: " + moduleId));
        Lesson lesson = new Lesson();
        lesson.setTitle(title);
        lesson.setContent(content);
        module.addLesson(lesson);
        return lessonRepository.save(lesson);
    }

    @Transactional(readOnly = true)
    public Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Курс не найден: " + id));
    }

    @Transactional
    public Course updateDescription(Long id, String description) {
        Course course = getCourse(id);
        course.setDescription(description);   // save не нужен: сущность в транзакции отслеживается
        return course;
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = getCourse(id);
        if (!course.getEnrollments().isEmpty()) {
            throw new BusinessException("Нельзя удалить курс, на который записаны студенты");
        }
        courseRepository.delete(course);
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseResponse(Long id) {
        return CourseResponse.from(getCourse(id));
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream().map(CourseResponse::from).toList();
    }
}