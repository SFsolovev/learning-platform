package com.example.learning_platform.repository;
import com.example.learning_platform.entity.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {
    List<CourseReview> findByCourseId(Long courseId);
}