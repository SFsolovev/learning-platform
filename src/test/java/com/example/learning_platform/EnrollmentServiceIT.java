package com.example.learning_platform;

import com.example.learning_platform.entity.Course;
import com.example.learning_platform.entity.Enrollment;
import com.example.learning_platform.entity.EnrollmentStatus;
import com.example.learning_platform.entity.Role;
import com.example.learning_platform.entity.User;
import com.example.learning_platform.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class EnrollmentServiceIT extends BaseIntegrationTest {

    @Test
    void studentEnrolls() {
        Course c = createCourse(createUser(Role.TEACHER));
        User s = createUser(Role.STUDENT);
        Enrollment e = enrollmentService.enrollStudent(c.getId(), s.getId());
        assertThat(e.getStatus()).isEqualTo(EnrollmentStatus.ACTIVE);
        assertThat(enrollmentService.getByCourse(c.getId())).hasSize(1);
        assertThat(enrollmentService.getByStudent(s.getId())).hasSize(1);
    }

    @Test
    void duplicateEnrollmentIsRejected() {
        Course c = createCourse(createUser(Role.TEACHER));
        User s = createUser(Role.STUDENT);
        enrollmentService.enrollStudent(c.getId(), s.getId());
        assertThatThrownBy(() -> enrollmentService.enrollStudent(c.getId(), s.getId()))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void teacherCannotEnroll() {
        User teacher = createUser(Role.TEACHER);
        Course c = createCourse(teacher);
        assertThatThrownBy(() -> enrollmentService.enrollStudent(c.getId(), teacher.getId()))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void unenrollRemovesEnrollment() {
        Course c = createCourse(createUser(Role.TEACHER));
        User s = createUser(Role.STUDENT);
        enrollmentService.enrollStudent(c.getId(), s.getId());
        enrollmentService.unenroll(c.getId(), s.getId());
        assertThat(enrollmentService.getByCourse(c.getId())).isEmpty();
    }
}