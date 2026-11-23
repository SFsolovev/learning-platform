package com.example.learning_platform.repository;
import com.example.learning_platform.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    List<Enrollment> findByCourseId(Long courseId);
    List<Enrollment> findByUserId(Long userId);
    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);
}