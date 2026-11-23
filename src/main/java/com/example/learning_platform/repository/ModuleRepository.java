package com.example.learning_platform.repository;

import com.example.learning_platform.entity.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ModuleRepository extends JpaRepository<CourseModule, Long> {
    List<CourseModule> findByCourseIdOrderByOrderIndex(Long courseId);
}
