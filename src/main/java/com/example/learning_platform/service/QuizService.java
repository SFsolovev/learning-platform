package com.example.learning_platform.service;

import com.example.learning_platform.dto.OptionData;
import com.example.learning_platform.entity.*;
import com.example.learning_platform.entity.UserRepository;
import com.example.learning_platform.exception.*;
import com.example.learning_platform.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.learning_platform.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {
    private final QuizRepository quizRepository;
    private final ModuleRepository moduleRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public Quiz createQuiz(Long moduleId, String title, int timeLimit) {
        CourseModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new NotFoundException("Модуль не найден: " + moduleId));
        if (quizRepository.findByModuleId(moduleId).isPresent()) {
            throw new BusinessException("У модуля уже есть квиз");
        }
        Quiz quiz = new Quiz();
        quiz.setModule(module);
        quiz.setTitle(title);
        quiz.setTimeLimit(timeLimit);
        return quizRepository.save(quiz);
    }

    @Transactional
    public Question addQuestion(Long quizId, String text, QuestionType type, List<OptionData> options) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Квиз не найден: " + quizId));
        if (options.stream().noneMatch(OptionData::correct)) {
            throw new BusinessException("Нужен хотя бы один правильный ответ");
        }
        Question q = new Question();
        q.setText(text);
        q.setType(type);
        for (OptionData o : options) {
            AnswerOption ao = new AnswerOption();
            ao.setText(o.text());
            ao.setCorrect(o.correct());
            q.addOption(ao);
        }
        quiz.addQuestion(q);
        return questionRepository.save(q);   // варианты сохранятся каскадом
    }

    /** answers: id вопроса -> список выбранных id вариантов */
    @Transactional
    public QuizSubmission takeQuiz(Long quizId, Long studentId, Map<Long, Set<Long>> answers) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Квиз не найден: " + quizId));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + studentId));

        Long courseId = quiz.getModule().getCourse().getId();
        if (!enrollmentRepository.existsByUserIdAndCourseId(studentId, courseId)) {
            throw new BusinessException("Студент не записан на курс этого квиза");
        }

        int score = 0;
        for (Question q : quiz.getQuestions()) {
            Set<Long> correctIds = q.getOptions().stream()
                    .filter(AnswerOption::isCorrect)
                    .map(AnswerOption::getId)
                    .collect(Collectors.toSet());
            Set<Long> chosen = answers.getOrDefault(q.getId(), Set.of());
            if (correctIds.equals(chosen)) {
                score++;                      // балл за полностью верный ответ
            }
        }
        QuizSubmission qs = new QuizSubmission();
        qs.setQuiz(quiz);
        qs.setStudent(student);
        qs.setScore(score);
        qs.setTakenAt(LocalDateTime.now());
        log.info("Студент {} прошёл квиз {}: {} баллов", studentId, quizId, score);
        return quizSubmissionRepository.save(qs);
    }

    @Transactional(readOnly = true)
    public List<QuizSubmission> getResults(Long quizId) {
        return quizSubmissionRepository.findByQuizId(quizId);
    }
}