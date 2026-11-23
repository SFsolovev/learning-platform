package com.example.learning_platform.service;

import com.example.learning_platform.dto.RatingResponse;
import com.example.learning_platform.dto.ReviewResponse;
import com.example.learning_platform.entity.*;
import com.example.learning_platform.entity.CourseRepository;
import com.example.learning_platform.entity.UserRepository;
import com.example.learning_platform.exception.BusinessException;
import com.example.learning_platform.exception.NotFoundException;
import com.example.learning_platform.repository.CourseReviewRepository;
import com.example.learning_platform.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final CourseReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public CourseReview addReview(Long courseId, Long studentId, int rating, String comment) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс не найден: " + courseId));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + studentId));
        if (rating < 1 || rating > 5) {
            throw new BusinessException("Оценка должна быть от 1 до 5");
        }
        if (!enrollmentRepository.existsByUserIdAndCourseId(studentId, courseId)) {
            throw new BusinessException("Отзыв может оставить только студент, записанный на курс");
        }
        if (reviewRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new BusinessException("Отзыв уже оставлен");
        }
        CourseReview r = new CourseReview();
        r.setCourse(course);
        r.setStudent(student);
        r.setRating(rating);
        r.setComment(comment);
        r.setCreatedAt(LocalDateTime.now());
        log.info("Студент {} оставил отзыв на курс {}", studentId, courseId);
        return reviewRepository.save(r);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviews(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new NotFoundException("Курс не найден: " + courseId);
        }
        return reviewRepository.findByCourseId(courseId).stream().map(ReviewResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public RatingResponse getRating(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new NotFoundException("Курс не найден: " + courseId);
        }
        Double avg = reviewRepository.averageRating(courseId);
        return new RatingResponse(avg == null ? 0.0 : avg, reviewRepository.countByCourseId(courseId));
    }
}