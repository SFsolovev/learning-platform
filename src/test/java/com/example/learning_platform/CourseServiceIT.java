package com.example.learning_platform;

import com.example.learning_platform.dto.CourseStructureResponse;
import com.example.learning_platform.dto.ModuleResponse;
import com.example.learning_platform.entity.*;
import com.example.learning_platform.exception.BusinessException;
import com.example.learning_platform.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class CourseServiceIT extends BaseIntegrationTest {

    @Test
    void teacherCreatesCourseWithModuleAndLesson() {
        User teacher = createUser(Role.TEACHER);
        Course c = createCourse(teacher);
        CourseModule m = courseService.addModule(c.getId(), "M1", 1);
        courseService.addLesson(m.getId(), "L1", "text");
        em.flush(); em.clear();

        Course loaded = courseRepository.findWithModulesById(c.getId()).orElseThrow();
        assertThat(loaded.getModules()).hasSize(1);
        assertThat(loaded.getTeacher().getId()).isEqualTo(teacher.getId());
    }

    @Test
    void studentCannotCreateCourse() {
        User student = createUser(Role.STUDENT);
        Long catId = createCategory().getId();
        assertThatThrownBy(() -> courseService.createCourse("X", "d", catId, student.getId()))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void unknownCategoryGivesNotFound() {
        User teacher = createUser(Role.TEACHER);
        assertThatThrownBy(() -> courseService.createCourse("X", "d", 999999L, teacher.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void deleteCourseCascadesToModules() {
        Course c = createCourse(createUser(Role.TEACHER));
        CourseModule m = courseService.addModule(c.getId(), "M1", 1);
        em.flush(); em.clear();

        courseService.deleteCourse(c.getId());
        em.flush();

        assertThat(moduleRepository.existsById(m.getId())).isFalse();
    }

    @Test
    void courseWithEnrollmentsCannotBeDeleted() {
        Course c = createCourse(createUser(Role.TEACHER));
        User student = createUser(Role.STUDENT);
        enrollmentService.enrollStudent(c.getId(), student.getId());
        em.flush(); em.clear();   // см. пояснение ниже

        assertThatThrownBy(() -> courseService.deleteCourse(c.getId()))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void courseStructureLoadsModulesWithLessonsInOrder() {
        Course c = createCourse(createUser(Role.TEACHER));
        CourseModule m2 = courseService.addModule(c.getId(), "Second", 2);
        CourseModule m1 = courseService.addModule(c.getId(), "First", 1);
        courseService.addLesson(m1.getId(), "L1", "text");
        em.flush(); em.clear();

        CourseStructureResponse s = courseService.getCourseStructure(c.getId());
        assertThat(s.modules()).extracting(ModuleResponse::title).containsExactly("First", "Second");
        assertThat(s.modules().get(0).lessons()).hasSize(1);
    }

    @Test
    void changeTeacherRejectsStudent() {
        Course c = createCourse(createUser(Role.TEACHER));
        User student = createUser(Role.STUDENT);
        assertThatThrownBy(() -> courseService.changeTeacher(c.getId(), student.getId()))
                .isInstanceOf(BusinessException.class);
    }
}