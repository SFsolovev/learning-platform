package com.example.learning_platform.repository;
import com.example.learning_platform.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {}