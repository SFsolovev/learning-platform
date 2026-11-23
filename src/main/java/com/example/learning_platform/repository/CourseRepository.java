package com.example.learning_platform.entity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCategoryId(Long categoryId);

    @Query("select c from Course c left join fetch c.modules where c.id = :id")
    Optional<Course> findWithModulesById(Long id);

    @Override
    @EntityGraph(attributePaths = {"category", "teacher"})
    List<Course> findAll();

    @EntityGraph(attributePaths = {"category", "teacher"})
    List<Course> findByTagsId(Long tagId);
}