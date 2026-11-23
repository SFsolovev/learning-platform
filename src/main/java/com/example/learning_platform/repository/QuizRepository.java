package com.example.learning_platform.repository;

import com.example.learning_platform.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByModuleId(Long moduleId);
}