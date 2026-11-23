package com.example.learning_platform.service;

import com.example.learning_platform.dto.AssignmentResponse;
import com.example.learning_platform.dto.SubmissionResponse;
import com.example.learning_platform.entity.*;
import com.example.learning_platform.entity.UserRepository;
import com.example.learning_platform.exception.*;
import com.example.learning_platform.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final LessonRepository lessonRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public Assignment createAssignment(Long lessonId, String title, String description,
                                       LocalDate dueDate, int maxScore) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Урок не найден: " + lessonId));
        Assignment a = new Assignment();
        a.setTitle(title);
        a.setDescription(description);
        a.setDueDate(dueDate);
        a.setMaxScore(maxScore);
        lesson.addAssignment(a);
        return assignmentRepository.save(a);
    }

    @Transactional
    public Submission submitAssignment(Long assignmentId, Long studentId, String content) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new NotFoundException("Задание не найдено: " + assignmentId));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + studentId));

        // курс достаём по цепочке связей: внутри транзакции LAZY работает
        Long courseId = assignment.getLesson().getModule().getCourse().getId();
        if (!enrollmentRepository.existsByUserIdAndCourseId(studentId, courseId)) {
            throw new BusinessException("Студент не записан на курс этого задания");
        }
        if (submissionRepository.existsByAssignmentIdAndStudentId(assignmentId, studentId)) {
            throw new BusinessException("Решение уже отправлено");
        }
        if (assignment.getDueDate() != null && LocalDate.now().isAfter(assignment.getDueDate())) {
            throw new BusinessException("Срок сдачи истёк");
        }
        Submission s = new Submission();
        s.setAssignment(assignment);
        s.setStudent(student);
        s.setContent(content);
        s.setSubmittedAt(LocalDateTime.now());
        log.info("Студент {} сдал задание {}", studentId, assignmentId);
        return submissionRepository.save(s);
    }

    @Transactional
    public Submission gradeSubmission(Long submissionId, int score, String feedback) {
        Submission s = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new NotFoundException("Решение не найдено: " + submissionId));
        int max = s.getAssignment().getMaxScore();
        if (score < 0 || score > max) {
            throw new BusinessException("Оценка должна быть от 0 до " + max);
        }
        s.setScore(score);
        s.setFeedback(feedback);
        return s;                       // dirty checking сам сохранит изменения
    }

    @Transactional(readOnly = true)
    public List<Submission> getSubmissions(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId);
    }

    @Transactional
    public SubmissionResponse grade(Long submissionId, int score, String feedback) {
        return SubmissionResponse.from(gradeSubmission(submissionId, score, feedback));
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAssignmentsByLesson(Long lessonId) {
        if (!lessonRepository.existsById(lessonId)) {
            throw new NotFoundException("Урок не найден: " + lessonId);
        }
        return assignmentRepository.findByLessonId(lessonId).stream()
                .map(AssignmentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> getSubmissionResponses(Long assignmentId) {
        if (!assignmentRepository.existsById(assignmentId)) {
            throw new NotFoundException("Задание не найдено: " + assignmentId);
        }
        return submissionRepository.findByAssignmentId(assignmentId).stream()
                .map(SubmissionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> getStudentSubmissions(Long studentId) {
        if (!userRepository.existsById(studentId)) {
            throw new NotFoundException("Пользователь не найден: " + studentId);
        }
        return submissionRepository.findByStudentId(studentId).stream()
                .map(SubmissionResponse::from).toList();
    }
}