package com.example.learning_platform;
import java.time.LocalDate;
import java.util.List;

import com.example.learning_platform.entity.*;
import com.example.learning_platform.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class AssignmentServiceIT extends BaseIntegrationTest {

    private Long courseId;
    private Long assignmentId;
    private User student;

    @BeforeEach
    void setUp() {
        Course c = createCourse(createUser(Role.TEACHER));
        courseId = c.getId();
        CourseModule m = courseService.addModule(courseId, "M1", 1);
        Lesson l = courseService.addLesson(m.getId(), "L1", "text");
        assignmentId = assignmentService
                .createAssignment(l.getId(), "HW", "desc", LocalDate.now().plusDays(7), 10).getId();
        student = createUser(Role.STUDENT);
    }

    @Test
    void enrolledStudentSubmits() {
        enrollmentService.enrollStudent(courseId, student.getId());
        Submission s = assignmentService.submitAssignment(assignmentId, student.getId(), "answer");
        assertThat(s.getId()).isNotNull();
        assertThat(s.getScore()).isNull();
    }

    @Test
    void notEnrolledStudentCannotSubmit() {
        assertThatThrownBy(() -> assignmentService.submitAssignment(assignmentId, student.getId(), "a"))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void secondSubmissionIsRejected() {
        enrollmentService.enrollStudent(courseId, student.getId());
        assignmentService.submitAssignment(assignmentId, student.getId(), "a");
        assertThatThrownBy(() -> assignmentService.submitAssignment(assignmentId, student.getId(), "b"))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void gradeStoresScoreAndFeedback() {
        enrollmentService.enrollStudent(courseId, student.getId());
        Submission s = assignmentService.submitAssignment(assignmentId, student.getId(), "a");
        assignmentService.gradeSubmission(s.getId(), 8, "good");
        em.flush(); em.clear();
        List<Submission> list = assignmentService.getSubmissions(assignmentId);
        assertThat(list.get(0).getScore()).isEqualTo(8);
        assertThat(list.get(0).getFeedback()).isEqualTo("good");
    }

    @Test
    void scoreAboveMaxIsRejected() {
        enrollmentService.enrollStudent(courseId, student.getId());
        Submission s = assignmentService.submitAssignment(assignmentId, student.getId(), "a");
        assertThatThrownBy(() -> assignmentService.gradeSubmission(s.getId(), 11, "x"))
                .isInstanceOf(BusinessException.class);
    }
}