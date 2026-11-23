package com.example.learning_platform.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modules")
@Getter @Setter @NoArgsConstructor
public class CourseModule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(nullable = false)
    private String title;
    private Integer orderIndex;
    private String description;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    @OneToOne(mappedBy = "module", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Quiz quiz;

    public void addLesson(Lesson l) { lessons.add(l); l.setModule(this); }
}