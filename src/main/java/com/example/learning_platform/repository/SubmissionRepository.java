package com.example.learning_platform.repository;
import com.example.learning_platform.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    boolean existsByAssignmentIdAndStudentId(Long assignmentId, Long studentId);
    List<Submission> findByAssignmentId(Long assignmentId);
    List<Submission> findByStudentId(Long studentId);
}