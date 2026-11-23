package com.example.learning_platform.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
@Getter @Setter @NoArgsConstructor
public class Enrollment{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDate enrollDate;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status; // ACTIVE, COMPLETED
}