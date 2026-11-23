package com.example.learning_platform;

import com.example.learning_platform.dto.RatingResponse;
import com.example.learning_platform.entity.Course;
import com.example.learning_platform.entity.CourseReview;
import com.example.learning_platform.entity.Role;
import com.example.learning_platform.entity.User;
import com.example.learning_platform.exception.BusinessException;
import com.example.learning_platform.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Transactional
class ReviewServiceIT extends BaseIntegrationTest {
    @Autowired
    ReviewService reviewService;

    private Course course;
    private User student;

    @BeforeEach
    void setUp() {
        course = createCourse(createUser(Role.TEACHER));
        student = createUser(Role.STUDENT);
    }

    @Test
    void enrolledStudentLeavesReview() {
        enrollmentService.enrollStudent(course.getId(), student.getId());
        CourseReview r = reviewService.addReview(course.getId(), student.getId(), 5, "great");
        assertThat(r.getId()).isNotNull();
        assertThat(reviewService.getReviews(course.getId())).hasSize(1);
    }

    @Test
    void notEnrolledStudentCannotReview() {
        assertThatThrownBy(() -> reviewService.addReview(course.getId(), student.getId(), 5, "x"))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void secondReviewIsRejected() {
        enrollmentService.enrollStudent(course.getId(), student.getId());
        reviewService.addReview(course.getId(), student.getId(), 4, "ok");
        assertThatThrownBy(() -> reviewService.addReview(course.getId(), student.getId(), 5, "again"))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void ratingOutOfRangeIsRejected() {
        enrollmentService.enrollStudent(course.getId(), student.getId());
        assertThatThrownBy(() -> reviewService.addReview(course.getId(), student.getId(), 6, "x"))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void averageRatingIsCalculated() {
        User second = createUser(Role.STUDENT);
        enrollmentService.enrollStudent(course.getId(), student.getId());
        enrollmentService.enrollStudent(course.getId(), second.getId());
        reviewService.addReview(course.getId(), student.getId(), 4, "a");
        reviewService.addReview(course.getId(), second.getId(), 5, "b");

        RatingResponse rating = reviewService.getRating(course.getId());
        assertThat(rating.count()).isEqualTo(2);
        assertThat(rating.average()).isEqualTo(4.5);
    }
}