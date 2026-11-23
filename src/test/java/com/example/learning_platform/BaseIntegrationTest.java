package com.example.learning_platform;

import com.example.learning_platform.entity.*;
import com.example.learning_platform.entity.CourseRepository;
import com.example.learning_platform.entity.UserRepository;
import com.example.learning_platform.repository.CategoryRepository;
import com.example.learning_platform.repository.ModuleRepository;
import com.example.learning_platform.service.AssignmentService;
import com.example.learning_platform.service.CourseService;
import com.example.learning_platform.service.EnrollmentService;
import com.example.learning_platform.service.QuizService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.learning_platform.entity.Role;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
    @Autowired
    protected UserRepository userRepository;
    @Autowired protected CategoryRepository categoryRepository;
    @Autowired protected CourseRepository courseRepository;
    @Autowired protected ModuleRepository moduleRepository;
    @Autowired protected CourseService courseService;
    @Autowired protected EnrollmentService enrollmentService;
    @Autowired protected AssignmentService assignmentService;
    @Autowired protected QuizService quizService;
    @PersistenceContext
    protected EntityManager em;

    protected User createUser(Role role) {
        User u = new User();
        u.setName(role.name());
        u.setEmail(UUID.randomUUID() + "@test.com");   // уникальный email в каждом тесте
        u.setRole(role);
        return userRepository.save(u);
    }

    protected Category createCategory() {
        Category c = new Category();
        c.setName("cat-" + UUID.randomUUID());
        return categoryRepository.save(c);
    }

    protected Course createCourse(User teacher) {
        return courseService.createCourse("Java", "desc", createCategory().getId(), teacher.getId());
    }
}