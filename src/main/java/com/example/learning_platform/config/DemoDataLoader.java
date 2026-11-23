package com.example.learning_platform.config;

import com.example.learning_platform.dto.OptionData;
import com.example.learning_platform.entity.*;
import com.example.learning_platform.repository.*;
import com.example.learning_platform.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;   // именно этот Profile, не из entity!
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DemoDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final QuizService quizService;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Демо-данные уже есть, пропускаем");
            return;
        }

        User teacher = saveUser("Анна Петрова", "teacher@demo.com", Role.TEACHER);
        User ivan = saveUser("Иван Сидоров", "ivan@demo.com", Role.STUDENT);
        User maria = saveUser("Мария Орлова", "maria@demo.com", Role.STUDENT);

        Category programming = saveCategory("Программирование");
        Category databases = saveCategory("Базы данных");

        Course java = saveCourse("Java для начинающих", "Основы языка Java и ООП",
                "8 недель", programming, teacher);
        Course sql = saveCourse("PostgreSQL с нуля", "Запросы, индексы и транзакции",
                "6 недель", databases, teacher);

        addContent(java.getId(), "Введение в Java", "Основы синтаксиса");
        addContent(java.getId(), "ООП", "Классы и объекты");
        addContent(sql.getId(), "Основы SQL", "SELECT, JOIN, GROUP BY");

        enrollmentService.enrollStudent(java.getId(), ivan.getId());
        enrollmentService.enrollStudent(java.getId(), maria.getId());
        enrollmentService.enrollStudent(sql.getId(), ivan.getId());

        createDemoQuiz(java.getId());

        log.info("Демо-данные загружены: 3 пользователя, 2 курса");
    }

    private void addContent(Long courseId, String moduleTitle, String moduleDescription) {
        int order = moduleRepository.findByCourseIdOrderByOrderIndex(courseId).size() + 1;
        courseService.addModule(courseId, moduleTitle, order, moduleDescription);
        // находим только что созданный модуль и добавляем уроки
        List<CourseModule> modules = moduleRepository.findByCourseIdOrderByOrderIndex(courseId);
        CourseModule module = modules.get(modules.size() - 1);
        courseService.addLesson(module.getId(), "Урок 1. Теория", "Текст урока", null);
        courseService.addLesson(module.getId(), "Урок 2. Практика", "Задачи для самостоятельной работы",
                "https://example.com/video");
    }

    private void createDemoQuiz(Long courseId) {
        CourseModule module = moduleRepository.findByCourseIdOrderByOrderIndex(courseId).get(0);
        quizService.createQuiz(module.getId(), "Тест по основам Java", 15);
        Long quizId = quizService.getQuizResponseByModule(module.getId()).id();
        quizService.addQuestion(quizId, "Какое ключевое слово создаёт объект?", QuestionType.SINGLE_CHOICE,
                List.of(new OptionData("new", true),
                        new OptionData("create", false),
                        new OptionData("make", false)));
    }

    private User saveUser(String name, String email, Role role) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setRole(role);
        return userRepository.save(u);
    }

    private Category saveCategory(String name) {
        Category c = new Category();
        c.setName(name);
        return categoryRepository.save(c);
    }

    private Course saveCourse(String title, String description, String duration,
                              Category category, User teacher) {
        Course c = new Course();
        c.setTitle(title);
        c.setDescription(description);
        c.setDuration(duration);
        c.setStartDate(LocalDate.now().plusDays(7));
        c.setCategory(category);
        c.setTeacher(teacher);
        return courseRepository.save(c);
    }
}