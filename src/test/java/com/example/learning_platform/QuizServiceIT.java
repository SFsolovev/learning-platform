package com.example.learning_platform;

import com.example.learning_platform.dto.OptionData;
import com.example.learning_platform.entity.*;
import com.example.learning_platform.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class QuizServiceIT extends BaseIntegrationTest {

    private Long courseId;
    private Quiz quiz;
    private Question question;
    private Long correctId;
    private Long wrongId;
    private User student;

    @BeforeEach
    void setUp() {
        courseId = createCourse(createUser(Role.TEACHER)).getId();
        CourseModule m = courseService.addModule(courseId, "M1", 1);
        quiz = quizService.createQuiz(m.getId(), "Quiz", 10);
        question = quizService.addQuestion(quiz.getId(), "2+2?", QuestionType.SINGLE_CHOICE,
                List.of(new OptionData("4", true), new OptionData("5", false)));
        correctId = question.getOptions().stream().filter(AnswerOption::isCorrect)
                .findFirst().orElseThrow().getId();
        wrongId = question.getOptions().stream().filter(o -> !o.isCorrect())
                .findFirst().orElseThrow().getId();
        student = createUser(Role.STUDENT);
        enrollmentService.enrollStudent(courseId, student.getId());
    }

    @Test
    void correctAnswerGivesPoint() {
        QuizSubmission qs = quizService.takeQuiz(quiz.getId(), student.getId(),
                Map.of(question.getId(), Set.of(correctId)));
        assertThat(qs.getScore()).isEqualTo(1);
    }

    @Test
    void wrongAnswerGivesZero() {
        QuizSubmission qs = quizService.takeQuiz(quiz.getId(), student.getId(),
                Map.of(question.getId(), Set.of(wrongId)));
        assertThat(qs.getScore()).isZero();
    }

    @Test
    void secondQuizInSameModuleIsRejected() {
        Long moduleId = quiz.getModule().getId();
        assertThatThrownBy(() -> quizService.createQuiz(moduleId, "Another", 5))
                .isInstanceOf(BusinessException.class);
    }
}