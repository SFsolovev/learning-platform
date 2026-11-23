package com.example.learning_platform;

import com.example.learning_platform.entity.Course;
import com.example.learning_platform.entity.Enrollment;
import com.example.learning_platform.entity.EnrollmentStatus;
import com.example.learning_platform.entity.Role;
import com.example.learning_platform.entity.User;
import com.example.learning_platform.exception.BusinessException;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LazyLoadingIT extends BaseIntegrationTest {

    private Long courseId;

    @BeforeEach
    void setUp() {
        courseId = createCourse(createUser(Role.TEACHER)).getId();
        courseService.addModule(courseId, "M1", 1);
    }

    @Test
    void lazyCollectionOutsideTransactionFails() {
        Course c = courseService.getCourse(courseId);       // сессия уже закрыта
        assertThatThrownBy(() -> c.getModules().size())
                .isInstanceOf(LazyInitializationException.class);
    }

    @Test
    void joinFetchLoadsModulesEagerlyForThisQuery() {
        Course c = courseRepository.findWithModulesById(courseId).orElseThrow();
        assertThat(c.getModules()).hasSize(1);               // модули подтянуты запросом
    }
}