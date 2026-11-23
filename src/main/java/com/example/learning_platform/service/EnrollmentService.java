package com.example.learning_platform.service;

import com.example.learning_platform.dto.EnrollmentResponse;
import com.example.learning_platform.entity.CourseRepository;
import com.example.learning_platform.entity.*;
import com.example.learning_platform.entity.UserRepository;
import com.example.learning_platform.exception.*;
import com.example.learning_platform.repository.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public Enrollment enrollStudent(Long courseId, Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + studentId));
        if (student.getRole() != Role.STUDENT) {
            throw new BusinessException("Записаться на курс может только студент");
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс не найден: " + courseId));
        if (enrollmentRepository.existsByUserIdAndCourseId(studentId, courseId)) {
            throw new BusinessException("Студент уже записан на этот курс");
        }
        Enrollment e = new Enrollment();
        e.setUser(student);
        e.setCourse(course);
        e.setEnrollDate(LocalDate.now());
        e.setStatus(EnrollmentStatus.ACTIVE);
        log.info("Студент {} записан на курс {}", studentId, courseId);
        return enrollmentRepository.save(e);
    }

    @Transactional
    public void unenroll(Long courseId, Long studentId) {
        Enrollment e = enrollmentRepository.findByUserIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new NotFoundException("Запись не найдена"));
        enrollmentRepository.delete(e);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getByStudent(Long studentId) {
        return enrollmentRepository.findByUserId(studentId);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getCourseEnrollments(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new NotFoundException("Курс не найден: " + courseId);
        }
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(EnrollmentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getStudentEnrollments(Long studentId) {
        if (!userRepository.existsById(studentId)) {
            throw new NotFoundException("Пользователь не найден: " + studentId);
        }
        return enrollmentRepository.findByUserId(studentId).stream()
                .map(EnrollmentResponse::from).toList();
    }
}