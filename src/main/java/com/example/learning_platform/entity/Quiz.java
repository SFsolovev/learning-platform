package com.example.learning_platform.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Quiz {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "module_id", unique = true)
    private CourseModule module;

    @Column(nullable = false)
    private String title;
    private Integer timeLimit;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quiz")
    private List<QuizSubmission> submissions = new ArrayList<>();

    public void addQuestion(Question q) { questions.add(q); q.setQuiz(this); }
}